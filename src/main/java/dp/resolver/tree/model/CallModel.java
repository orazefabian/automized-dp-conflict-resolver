package dp.resolver.tree.model;

import dp.resolver.base.ImplSpoon;
import dp.resolver.tree.element.CallNode;
import dp.resolver.tree.element.Invocation;
import org.apache.maven.pom._4_0.Dependency;
import org.apache.maven.pom._4_0.Model;
import spoon.Launcher;
import spoon.MavenLauncher;
import spoon.SpoonException;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.code.CtLocalVariableImpl;
import spoon.support.reflect.declaration.CtMethodImpl;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*********************************
 Created by Fabian Oraze on 12.02.21
 *********************************/

public abstract class CallModel {


    private final List<Invocation> leafInvocations;
    protected CtModel ctModel;
    protected final List<String> classNames;
    protected final Map<String, Boolean> jarPaths;
    protected final String currProjectPath;
    protected List<ImplSpoon> pomModels; // holds all possible pom models of sub modules
    protected Launcher launcher;
    protected List<CallNode> callNodes;
    protected ImplSpoon baseModel; // the base pom model from the root project
    private String pathM2;

    protected CallModel(String pathToProject, List<Invocation> leafInvocations) {
        this.pomModels = new ArrayList<>();
        this.classNames = new ArrayList<>();
        this.jarPaths = new HashMap<>();
        this.currProjectPath = pathToProject;
        this.callNodes = new ArrayList<>();
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

    public List<CallNode> getCallNodes() {
        return callNodes;
    }

    public void setCallNodes(List<CallNode> callNodes) {
        this.callNodes = callNodes;
    }

    /**
     * function which initializes all local class names for current model
     */
    protected void initClassNames() {
        this.classNames.clear();
        for (Object type : this.ctModel.filterChildren(new TypeFilter<>(CtType.class)).list()) {
            CtType c = (CtType) type;
            this.classNames.add(c.getSimpleName());
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
        Model effPom = getEffectivePomModel(model);
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
     * creates effective pom via the ImplSpoon object and retrieves the version from it
     *
     * @param model the ImplSpoon model which refers to a certain pom.xml
     * @return the version from the effective pom
     * @throws JAXBException        if marshalling fails
     * @throws IOException          if reading or writing file fails
     * @throws InterruptedException if process gets interrupted
     */
    private Model getEffectivePomModel(ImplSpoon model) throws JAXBException, IOException, InterruptedException {
        File currPro = new File(model.getPath());
        String currPath;
        boolean fromMaven;
        if (this.launcher instanceof MavenLauncher) {
            currPath = currPro.getAbsolutePath();
            fromMaven = true;
        } else {
            currPath = currPro.getAbsolutePath().substring(0, currPro.getAbsolutePath().lastIndexOf(File.separator));
            fromMaven = false;
        }
        File pom = new File(currPath + File.separator + "pom.xml");
        if (!pom.exists()) {
            // must write pom.xml file before creating effective pom, because it does not recognize .pom endings
            model.writePom(new File(currPath + File.separator + "pom.xml"), model.getPomModel());
        }
        File effectivePom = model.createEffectivePom(currPro, fromMaven);
        Model pomModel = model.createEffectivePomModel(effectivePom);

        return pomModel;
    }


    public List<CallNode> analyzeModel() {
        // iterate over all classes in model
        System.out.println("Iterating over classes...");
        for (Object type : this.ctModel.filterChildren(new TypeFilter<>(CtType.class)).list()) {
            CtType s = (CtType) type;
            try {
                System.out.println("Searching class: " + s.getSimpleName());
                for (Object obj : s.filterChildren(new TypeFilter<CtMethod>(CtMethod.class)).list()) {
                    CtMethodImpl m = (CtMethodImpl) obj;
                    //if (checkMethodFromCallChain(m, leafInvocations)) {
                    //System.out.println("    Checking body of method: " + m.getSimpleName());
                    searchInvocation(m, s);
                    //}
                }
            } catch (SpoonException | NullPointerException e) {
                System.err.println("could not iterate over methods in class: " + s.getSimpleName());
            }
        }
        return this.callNodes;
    }

    /**
     * helper function that check if a method is part of current call chain
     *
     * @param method currently iterated method
     * @return true if method is part of call chain
     */
    private boolean checkMethodFromCallChain(CtMethod method) {
        if (this.leafInvocations == null) return true;
        for (Invocation invocation : this.leafInvocations) {
            if (invocation.getMethodSignature().split("\\(")[0].equals(method.getSimpleName())
                    && !checkJDKClasses(method.getDeclaringType().getQualifiedName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * called by iterateClasses for each method that is part of call chain, than searches for invocations that point to non-local classes and if needed adds them
     * to the call chain of the call tree
     *
     * @param method    current method to analyze
     * @param currClass String signature of class which current method belongs to
     */
    private void searchInvocation(CtMethod method, CtType currClass) throws NullPointerException {
        // get all method body elements
        String currClassName = currClass.getQualifiedName();

        List<CtInvocation> methodCalls = method.getElements(new TypeFilter<>(CtInvocation.class));
        List<CtConstructorCall> constructorCalls = method.filterChildren(new TypeFilter<>(CtConstructorCall.class)).list();
        CallNode currNode = null;
        // creates new Node from and if needed appends it to a leaf
        if (methodCalls.size() != 0 || constructorCalls.size() != 0 || currClass.toString().contains("interface " + currClass.getSimpleName())) {
            currNode = getNodeByName(currClassName);
            if (this.leafInvocations != null) appendNodeToLeaf(currNode);
        }
        // adds invocations called by current method to the current CallNode
        for (CtInvocation element : methodCalls) {
            CtTypeReference fromType;
            if (element.getExecutable().getType() == null || element.getExecutable().getType().toString().equals("void") || element.getExecutable().getType().isPrimitive()) {
                fromType = element.getExecutable().getDeclaringType();
            } else {
                fromType = element.getExecutable().getType();
            }
            if (!checkJDKClasses(fromType.getQualifiedName())) {
                // if maven project is analyzed and the referred Object from the curr method is contained in the project
                if (this.launcher instanceof MavenLauncher && this.classNames.contains(fromType.getSimpleName())) break;
                String methodSignature = element.getExecutable().toString();
                Invocation invocation = new Invocation(methodSignature, fromType.getQualifiedName(), currNode);
                currNode.addInvocation(invocation);
                // checks if invocations may refer to an interface and changes it to the actual implementation object
                checkIfInterfaceIsReferenced(invocation, constructorCalls);
            }
        }
        // delete node if it has no outgoing invocations
        if (currNode != null && currNode.getInvocations().size() == 0) currNode = null;
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
     * helper function that gets a CallNode from local list of callNodes
     *
     * @param currClass String name of current class
     * @return {@link CallNode}
     */
    private CallNode getNodeByName(String currClass) {
        for (CallNode n : this.callNodes) {
            if (n.getClassName().equals(currClass) && n.getFromJar().equals(this.currProjectPath)) {
                if (n.getPrevious() == null) return n;
            }
        }
        return getCallNode(currClass, this.currProjectPath);
    }

    /**
     * creates a new call node and appends it to class list of callNodes
     *
     * @param currClass the class which the node should be bound to
     * @param path      the path of the curr jar
     * @return a {@link CallNode}
     */
    private CallNode getCallNode(String currClass, String path) {
        CallNode currNode = new CallNode(currClass, path, this.jarPaths.keySet(), null);
        this.callNodes.add(currNode);
        return currNode;
    }

    /**
     * helper function to append a CallNode to the correct Invocation from the leaf elements
     *
     * @param currNode CallNode which corresponds to current Class
     */
    private void appendNodeToLeaf(CallNode currNode) {
        for (Invocation invocation : this.leafInvocations) {
            if (currNode.getClassName().contains(invocation.getDeclaringType())
                    && invocation.getParentNode().getCurrPomJarDependencies().contains(currNode.getFromJar()) && invocation.getNextNode() == null) {
                invocation.setNextNode(currNode);
                if (currNode.getPrevious() == null) currNode.setPrevious(invocation.getParentNode());
                // must check if parent node of invocations is same as the previous node of the nextNode
                if (!invocation.getParentNode().getFromJar().equals(currNode.getPrevious().getFromJar())) {
                    String clName = currNode.getClassName();
                    String path = currNode.getFromJar();
                    currNode = getCallNode(clName, path);
                    invocation.setNextNode(currNode);
                    invocation.getNextNode().setPrevious(invocation.getParentNode());
                }
            }
        }
    }

    /**
     * helper function which checks if a class is part of the JDK
     *
     * @param qualifiedName String name of class
     * @return true if class is part of JDK
     */
    private boolean checkJDKClasses(String qualifiedName) {
        return (qualifiedName.startsWith("java.") || (qualifiedName.startsWith("javax.xml.parsers")
                || (qualifiedName.startsWith("com.sun")) || (qualifiedName.startsWith("sun"))
                || (qualifiedName.startsWith("oracle")) || (qualifiedName.startsWith("org.xml"))
                || (qualifiedName.startsWith("com.oracle")) || (qualifiedName.startsWith("jdk"))
                || (qualifiedName.startsWith("javax.xml.stream"))));
    }

    public String getCurrProjectPath() {
        return this.currProjectPath;
    }
}
