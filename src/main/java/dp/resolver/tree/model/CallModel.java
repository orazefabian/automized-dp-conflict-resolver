package dp.resolver.tree.model;

import dp.resolver.base.ImplSpoon;
import dp.resolver.tree.JDKClassHelper;
import dp.resolver.tree.element.CallNode;
import dp.resolver.tree.element.Invocation;
import dp.resolver.tree.model.entity.MethodConnection;
import dp.resolver.tree.model.entity.MethodConnectionSet;
import org.apache.maven.pom._4_0.Dependency;
import org.apache.maven.pom._4_0.Model;
import spoon.Launcher;
import spoon.MavenLauncher;
import spoon.SpoonException;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtAbstractInvocation;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.NamedElementFilter;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.code.CtLocalVariableImpl;
import spoon.support.reflect.declaration.CtMethodImpl;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/*********************************
 Created by Fabian Oraze on 12.02.21
 *********************************/

@SuppressWarnings("rawtypes")
public abstract class CallModel {


    private final Set<Invocation> leafInvocations;
    private final List<String> allAnnotations;
    private final List<String> classesToTraverseAgain;
    private final MethodConnectionSet methodConnections;
    protected final List<String> classNames;
    protected final Map<String, Boolean> jarPaths;
    protected final String currProjectPath;
    protected CtModel ctModel;
    protected List<ImplSpoon> pomModels; // holds all possible pom models of sub modules
    protected Launcher launcher;
    protected List<CallNode> callNodes;
    protected ImplSpoon baseModel; // the base pom model from the root project
    private String pathM2;

    protected CallModel(String pathToProject, Set<Invocation> leafInvocations) {
        this.pomModels = new ArrayList<>();
        this.classNames = new ArrayList<>();
        this.jarPaths = new HashMap<>();
        this.currProjectPath = pathToProject;
        this.callNodes = new ArrayList<>();
        this.allAnnotations = new ArrayList<>();
        this.classesToTraverseAgain = new ArrayList<>();
        this.methodConnections = new MethodConnectionSet();
        this.leafInvocations = leafInvocations;
        setPathM2();
    }

    /**
     * should get all pom files from spoon model, also from sub modules, important for a maven launcher
     *
     * @param file the file which should be checked for pom occurrences
     */
    protected void searchModulesForPom(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                if (f.isDirectory()) {
                    // recursive call of next directory search
                    searchModulesForPom(f);
                } else if (f.getName().equalsIgnoreCase("pom.xml") && !f.getAbsolutePath().equals(this.currProjectPath)) {
                    this.pomModels.add(new ImplSpoon(f.getParentFile().getAbsolutePath() + File.separator));
                }
            }
        }
    }

    public List<String> getAllAnnotations() {
        return allAnnotations;
    }

    public List<CallNode> getCallNodes() {
        return callNodes;
    }


    /**
     * function which initializes all local class names for current model
     */
    protected void initClassNames() {
        this.classNames.clear();
        for (Object type : this.ctModel.filterChildren(new TypeFilter<>(CtType.class)).list()) {
            CtType c = (CtType) type;
            this.classNames.add(c.getQualifiedName());
        }
    }

    /**
     * creates path to local repository folder where maven dependency jars are saved
     */
    private void setPathM2() {
        String user = System.getProperty("user.name");
        if (System.getProperty("os.name").startsWith("Mac")) {
            this.pathM2 = "/Users/" + user + "/.m2/repository/";
        } else if (System.getProperty("os.name").startsWith("Windows")) {
            this.pathM2 = "C:\\Users\\" + user + "\\.m2\\repository\\";
        } else {
            this.pathM2 = "/home/" + user + "/.m2/repository/";
        }
    }

    /**
     * checks all pom files from current model and computes all dependencies to other jars
     *
     * @return a map with keys from all jarPaths referenced in curr and an initial bool value set to true
     * @throws NullPointerException
     * @throws IOException
     * @throws InterruptedException
     * @throws JAXBException
     */
    public Map<String, Boolean> getDependenciesToJarPaths() throws NullPointerException, IOException, InterruptedException, JAXBException {
        this.jarPaths.clear();
        checkModelForDPs(this.baseModel);
        for (ImplSpoon model : this.pomModels) {
            checkModelForDPs(model);
        }
        return this.jarPaths;
    }

    /**
     * analyzes an model for its dependencies and adds them to local jarPaths list
     *
     * @param model {@link ImplSpoon}
     * @throws JAXBException        if marshalling fails
     * @throws IOException          if reading or writing pom fails
     * @throws InterruptedException if process gets interrupted
     */
    private void checkModelForDPs(ImplSpoon model) throws JAXBException, IOException, InterruptedException {
        Model effPom = model.getEffectivePomModel(this.launcher);
        try {
            for (Dependency dp : model.getPomModel().getDependencies().getDependency()) {
                String version = null;
                for (Dependency dpEff : effPom.getDependencies().getDependency()) {
                    if (dpEff.getGroupId().equals(dp.getGroupId()) && dpEff.getArtifactId().equals(dp.getArtifactId())) {
                        // set the correct version
                        version = dpEff.getVersion();
                    }
                }
                String postFixJar = dp.getArtifactId() + "-" + version + ".jar";
                String currPath;
                if (System.getProperty("os.name").startsWith("Windows")) {
                    currPath = this.pathM2 + (dp.getGroupId().replace('.', '\\') + "\\" + dp.getArtifactId()) + "\\" + version + "\\" + postFixJar;
                } else {
                    currPath = this.pathM2 + (dp.getGroupId().replace('.', '/') + "/" + dp.getArtifactId()) + "/" + version + "/" + postFixJar;
                }

                this.jarPaths.put(currPath, false);
            }
        } catch (NullPointerException e) {
            System.err.println("No dependencies, skipping pom...");
        }
    }

    /**
     * main method which starts analyzing the current model for its call-chain
     * creates new {@link CallNode} and appends them to previous invocations
     * creates new {@link Invocation} and appends them to correct CallNodes
     */
    public void analyzeModel() {
        System.out.println("Iterating over classes...");
        for (Object type : this.ctModel.filterChildren(new TypeFilter<>(CtType.class)).list()) {
            CtType clazz = (CtType) type;
            try {
                iterateOverClass(clazz);
            } catch (SpoonException | NullPointerException e) {
                e.printStackTrace();
                System.err.println("could not iterate over methods in class: " + clazz.getSimpleName());
            }
        }
        searchLeftClassesToBeTraversed();
    }

    /**
     * checks if there are still classes to be traversed again, if so it calls the iterateOverClass method on the class which should
     * be analyzed again and removes it from the list, after finishing it calls itself until there are no more classes to be traversed
     */
    private void searchLeftClassesToBeTraversed() {
        if (!this.classesToTraverseAgain.isEmpty()) {
            String nameClass = this.classesToTraverseAgain.get(0);
            this.classesToTraverseAgain.remove(nameClass);
            for (Object type : this.ctModel.filterChildren(new NamedElementFilter<>(CtType.class, nameClass)).list()) {
                CtType clazz = (CtType) type;
                try {
                    iterateOverClass(clazz);
                } catch (SpoonException | NullPointerException e) {
                    e.printStackTrace();
                    System.err.println("could not iterate over methods in class: " + clazz.getSimpleName());
                }
            }
            searchLeftClassesToBeTraversed();
        }
    }

    /**
     * given a class, this method iterates over the elements of that class and analyzes methods and constructors
     *
     * @param clazz {@link CtType}
     * @throws SpoonException       when Spoon throws an internal error
     * @throws NullPointerException could happen when leaf invocations are malformed
     */
    private void iterateOverClass(CtType clazz) throws SpoonException, NullPointerException {
        List<Invocation> toBeAppended = new ArrayList<>();
        if (checkIfPossibleCallNode(clazz, toBeAppended)) {
            System.out.println("Searching class: " + clazz.getSimpleName());
            List<CallNode> currNodes = createCallNodesForClass(clazz, toBeAppended);
            for (CallNode currNode : currNodes) {
                // iterate methods
                for (Object obj : clazz.filterChildren(new TypeFilter<>(CtMethod.class)).list()) {
                    CtMethodImpl method = (CtMethodImpl) obj;
                    if (checkIfInvocationCallToMethod(method, currNode)) {
                        searchMethodForInvocations(method, currNode);
                    }
                }
                // iterate constructors
                for (Object obj : clazz.filterChildren(new TypeFilter<>(CtConstructor.class)).list()) {
                    CtConstructor constructor = (CtConstructor) obj;
                    if (checkIfInvocationCallToConstructor(constructor, currNode)) {
                        searchConstructorForInvocations(constructor, currNode);
                    }
                }
                searchClassForAnnotations(clazz);
            }
        }
    }

    private boolean checkIfInvocationCallToConstructor(CtConstructor constructor, CallNode currNode) {
        if (isRootProject()) return true;
        boolean isCalled = false;
        for (Invocation call : currNode.getPrevious().getInvocations()) {
            if (constructor.getSignature().endsWith(call.getMethodSignature())) {
                isCalled = true;
                break;
            }
        }
        return isCalled;
    }

    private boolean checkIfInvocationCallToMethod(CtMethod method, CallNode currNode) {
        if (isRootProject()) return true;
        boolean isCalled = false;
        for (Invocation call : currNode.getPrevious().getInvocations()) {
            if (call.getMethodSignature().equals(method.getSignature()) && call.getNextNode() == currNode) {
                isCalled = true;
                break;
            }
        }
        return isCalled;
    }

    /**
     * creates nodes for current classes and appends them to the given invocations
     *
     * @param clazz        the Current Class
     * @param toBeAppended list of invocation where the nodes then should be appended
     * @return the list of the newly created CallNodes
     */
    private List<CallNode> createCallNodesForClass(CtType clazz, List<Invocation> toBeAppended) {
        List<CallNode> nodes = new ArrayList<>();
        if (toBeAppended.size() == 0) {
            nodes.add(getNodeByName(clazz.getQualifiedName()));
        } else {
            for (Invocation mustAppend : toBeAppended) {
                CallNode nodeNew = getNewCallNode(clazz.getQualifiedName());
                appendOldOrNewNodeToLeaf(nodeNew, mustAppend, nodes);
            }
        }
        return nodes;
    }

    /**
     * check whether the class is referenced by any leaf invocation, if so the it appends the leaves to a list
     *
     * @param clazz                 the class that possibly will become a CallNode
     * @param neededLeafInvocations a list that is filled with the invocations that are referencing the current Class
     * @return true if the class should be a CallNode
     */
    private boolean checkIfPossibleCallNode(CtType clazz, List<Invocation> neededLeafInvocations) {
        if (isRootProject()) return true;
        if (this.leafInvocations.size() == 0) {
            return true;
        }
        boolean needed = false;
        for (Invocation invocation : this.leafInvocations) {
            if (checkIfMustBeAppended(clazz, invocation)) {
                needed = true;
                neededLeafInvocations.add(invocation);
            }
        }
        return needed;
    }


    /**
     * searches a class for annotations and if it finds any they are appended to the allAnnotations list
     *
     * @param currClass {@link CtType}
     */
    private void searchClassForAnnotations(CtType currClass) {
        List<CtAnnotation> annotations = currClass.filterChildren(new TypeFilter<>(CtAnnotation.class)).list();
        for (CtAnnotation annotation : annotations) {
            String annotationType = annotation.getAnnotationType().toString();
            if (!JDKClassHelper.isPartOfJDKClassesFromQualifiedName(annotationType)) {
                this.allAnnotations.add(annotationType);
            }
        }
    }

    /**
     * called by iterateClasses for each method that is part of call chain, than searches for invocations and if needed adds them
     * to the call chain of the call tree
     *
     * @param method   current method to analyze
     * @param currNode CallNode object that represents the current class
     */
    private void searchMethodForInvocations(CtMethod method, CallNode currNode) {
        if (isMethodCalledByCallNode(method, currNode)) {
            List<CtAbstractInvocation> methodCalls = method.getElements(new TypeFilter<>(CtAbstractInvocation.class));
            List<CtConstructorCall> constructorCalls = method.filterChildren(new TypeFilter<>(CtConstructorCall.class)).list();
            // adds invocations called by current method to the current CallNode
            for (CtAbstractInvocation methodCall : methodCalls) {
                try {
                    addPossibleInvocation(methodCall, constructorCalls, currNode);
                } catch (NullPointerException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    /**
     * called by iterateClasses for each constructor that is part of call chain, than searches for invocations and if needed adds them
     * to the call chain of the call tree
     *
     * @param constructor current constructor element to be analyzed
     * @param currNode    CallNode object that represents the current class
     */
    private void searchConstructorForInvocations(CtConstructor constructor, CallNode currNode) {
        List<CtAbstractInvocation> methodCalls = constructor.getElements(new TypeFilter<>(CtAbstractInvocation.class));
        List<CtConstructorCall> constructorCalls = constructor.filterChildren(new TypeFilter<>(CtConstructorCall.class)).list();
        // adds invocations called by current method to the current CallNode
        for (CtAbstractInvocation methodCall : methodCalls) {
            addPossibleInvocation(methodCall, constructorCalls, currNode);
        }
    }

    private boolean isMethodCalledByCallNode(CtMethod method, CallNode currNode) {
        if (isRootProject()) return true;
        for (Invocation call : currNode.getPrevious().getInvocations()) {
            if (call.getMethodSignature().equals(method.getSignature())
                    && call.getDeclaringType().equals(method.getDeclaringType().getQualifiedName())) {
                return true;
            } else if (this.methodConnections.isTransitiveReferenced(currNode.getClassName(), method.getSignature())) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks whether a call from a list of method calls should be appended as an invocation to a CallNode
     *
     * @param call             {@link CtInvocation} outgoing call inside a method
     * @param constructorCalls {@link CtConstructorCall} list of all constructor calls inside a method, is used to check if an invocation
     *                         is referring to an interface
     * @param currNode         {@link CallNode} the curr node which should be the parentNode of the possibly appended invocations
     */
    private void addPossibleInvocation(CtAbstractInvocation call, List<CtConstructorCall> constructorCalls, CallNode currNode) throws NullPointerException {
        CtTypeReference fromType;
        fromType = extractTargetTypeFromElement(call);
        if (!JDKClassHelper.isPartOfJDKClassesFromQualifiedName(fromType.getQualifiedName()) && checkForValidDeclaringType(fromType.getQualifiedName())) {
            // if maven project is analyzed and the referred Object from the curr method is contained in the project
            if (!(isRootProject() && this.classNames.contains(fromType.getQualifiedName()))) {
                String methodSignature = getMethodSignature(call);
                Invocation invocation = new Invocation(methodSignature, fromType.getQualifiedName(), currNode);
                if (shouldAddInvocationToCallNode(invocation, currNode)) {
                    currNode.addInvocation(invocation);
                    // checks if invocations may refer to an interface and changes it to the actual implementation object
                    checkIfInterfaceIsReferenced(invocation, constructorCalls);
                    if (this.classNames.contains(invocation.getDeclaringType())) {
                        appendToBeTraversedClass(invocation);
                    }
                } else {
                    appendToBeTraversedClass(invocation);
                }
                MethodConnection connection = new MethodConnection(invocation.getParentNode().getClassName(), invocation.getMethodSignature(), invocation.getDeclaringType());
                this.methodConnections.addConnection(connection);
            }
        }
    }

    private boolean isRootProject() {
        return this.launcher instanceof MavenLauncher;
    }

    private void appendToBeTraversedClass(Invocation invocation) {
        if (this.methodConnections.hasChangedSinceLastCheck()) {
            String nameClass = invocation.getDeclaringType().substring(invocation.getDeclaringType().lastIndexOf(".") + 1);
            if (!this.classesToTraverseAgain.contains(nameClass)) {
                this.classesToTraverseAgain.add(nameClass);
            }
        }
    }

    /**
     * checks whether to add the invocation to the given CallNode if the invocation is not already appended
     *
     * @param invocation {@link Invocation}
     * @param currNode   {@link CallNode}
     * @return true if it should be appended
     */
    private boolean shouldAddInvocationToCallNode(Invocation invocation, CallNode currNode) {
        if (!currNode.getInvocations().contains(invocation)
                && this.methodConnections.isClassAndDeclaringTypePresent(currNode.getClassName(), invocation.getDeclaringType(), invocation.getMethodSignature())) {
            if (!this.leafInvocations.contains(invocation)) {
                this.leafInvocations.add(invocation);
            }
            return true;
        }
        return false;
    }

    /**
     * get the signature from a CtAbstractInvocation that can be a method call or a constructor call
     *
     * @param element invocation element
     * @return the method signature as a String representation
     */
    private String getMethodSignature(CtAbstractInvocation element) {
        String signature = element.getExecutable().toString();
        if (signature.split("\\(")[0].contains(".")) {
            String[] construct = signature.split("\\(");
            String suffix = construct[1];
            String prefix = construct[0].substring(construct[0].lastIndexOf(".") + 1);
            StringBuilder builder = new StringBuilder();
            builder.append(prefix);
            builder.append("(");
            builder.append(suffix);
            signature = builder.toString();
        }
        return signature;
    }

    /**
     * gets the declaring target from an invocation (on what object is the method or constructor called upon)
     *
     * @param element the invocation element (can be a method or constructor call)
     * @return a {@link CtTypeReference} which refers to the target of the call
     * @throws NullPointerException in case the declaring target is Null
     */
    private CtTypeReference extractTargetTypeFromElement(CtAbstractInvocation element) throws NullPointerException {
        CtTypeReference fromType;
        fromType = element.getExecutable().getDeclaringType();
        if (fromType == null) throw new NullPointerException("No fromType for: " + element.toString());
        return fromType;
    }

    private boolean checkForValidDeclaringType(String qualifiedName) {
        return !qualifiedName.equals("?");
    }

    /**
     * check if the declaring type of a invocations refers to a interface and if so, it is switched with the correct object from a constructor call
     *
     * @param invocation       the newly created invocations
     * @param constructorCalls a list of {@link CtConstructorCall}
     */
    private void checkIfInterfaceIsReferenced(Invocation invocation, List<CtConstructorCall> constructorCalls) {
        try {
            for (CtConstructorCall call : constructorCalls) {
                if (invocation.getDeclaringType().equals(call.getParent(CtLocalVariableImpl.class).getType().toString())) {
                    invocation.setDeclaringType(call.getExecutable().getDeclaringType().toString());
                }
            }
        } catch (NullPointerException ignored) {
        }
    }

    /**
     * helper function that gets a CallNode from local list of callNodes, if no node with the className is present a new is created and returned
     *
     * @param currClass String name of current class
     * @return {@link CallNode}
     */
    private CallNode getNodeByName(String currClass) {
        int indexOfNode = Collections.binarySearch(this.callNodes, new CallNode(currClass, null, null, null));
        if (indexOfNode >= 0) {
            CallNode node = this.callNodes.get(indexOfNode);
            if (node.isLeafNode()) {
                return node;
            }
        }
        return getNewCallNode(currClass);
    }


    /**
     * creates a new call node and appends it to the list of local callNodes
     *
     * @param currClass the class which the node should be bound to
     * @return a {@link CallNode}
     */
    private CallNode getNewCallNode(String currClass) {
        CallNode currNode = new CallNode(currClass, this.currProjectPath, this.jarPaths.keySet(), null);
        this.callNodes.add(currNode);
        Collections.sort(this.callNodes);
        return currNode;
    }

    /**
     * helper function to append a CallNode to the correct Invocation from the leaf elements
     *
     * @param currNode   CallNode which corresponds to current Class
     * @param invocation leafInvocation that should be used for the CallNode to be appended to
     * @param nodes      list of newly created Nodes
     */
    private void appendOldOrNewNodeToLeaf(CallNode currNode, Invocation invocation, List<CallNode> nodes) {
        if (invocation.getParentNode().getClassName().equals(currNode.getClassName())) {
            CallNode newCallNode = getNewCallNode(currNode.getClassName());
            appendNodeToInvocation(newCallNode, invocation);
            nodes.add(newCallNode);
        } else {
            appendNodeToInvocation(currNode, invocation);
            nodes.add(currNode);
        }
    }

    private void appendNodeToInvocation(CallNode currNode, Invocation invocation) {
        invocation.setNextNode(currNode);
        if (currNode.getPrevious() == null) currNode.setPrevious(invocation.getParentNode());
        // must check if parent node of invocations is same as the previous node of the nextNode
        if (!invocation.getParentNode().getFromJar().equals(currNode.getPrevious().getFromJar())) {
            invocation.setNextNode(currNode);
            invocation.getNextNode().setPrevious(invocation.getParentNode());
        }
    }


    private boolean checkIfMustBeAppended(CtType currNode, Invocation invocation) {
        return currNode.getQualifiedName().contains(invocation.getDeclaringType())
                && ((invocation.getParentNode().getCurrPomJarDependencies().contains(this.currProjectPath))
                || invocation.getParentNode().getFromJar().equals(this.currProjectPath))
                && invocation.getNextNode() == null;
    }

    public String getCurrProjectPath() {
        return this.currProjectPath;
    }
}
