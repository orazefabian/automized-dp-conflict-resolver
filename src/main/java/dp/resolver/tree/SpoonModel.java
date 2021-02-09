package dp.resolver.tree;

import dp.resolver.base.ImplSpoon;
import dp.api.maven.CentralMavenAPI;
import dp.resolver.tree.element.CallNode;
import dp.resolver.tree.element.Invocation;
import org.apache.maven.pom._4_0.Dependency;
import org.apache.maven.pom._4_0.Model;
import spoon.JarLauncher;
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
import java.io.*;
import java.util.*;

/*********************************
 Created by Fabian Oraze on 27.11.20
 *********************************/

public class SpoonModel implements CallModel {

    private List<ImplSpoon> pomModels; // holds all possible pom models of sub modules
    private ImplSpoon baseModel; // the base pom model from the root project
    private Launcher launcher;
    private final CtModel ctModel;
    private String pathM2;
    private final List<String> classNames;
    private final Map<String, Boolean> jarPaths;
    private final String currProjectPath;
    private List<CallNode> callNodes;


    /**
     * object which builds a new spoon launcher which provides a AST
     *
     * @param pathToProject  String to a project, can be maven root folder or path to .jar file
     * @param analyzeFromJar boolean whether the pathToProject is a .jar file
     * @throws Exception if building the spoon model fails
     */
    public SpoonModel(String pathToProject, boolean analyzeFromJar) throws Exception {
        this.currProjectPath = pathToProject;
        setPathM2();
        this.classNames = new ArrayList<>();
        this.jarPaths = new HashMap<>();
        this.pomModels = new ArrayList<>();
        System.out.println("Starting to build spoon model from " + pathToProject + "...");
        initLauncherAndCreatePomModels(analyzeFromJar);
        this.ctModel = this.launcher.buildModel();
        System.out.println("Building spoon model finished");
        callNodes = new ArrayList<>();
        initClassNames();
        try {
            computeJarPaths();
        } catch (NullPointerException e) {
            System.err.println("No dependencies for project: " + pathToProject);
        }
    }

    @Override
    public String getCurrProjectPath() {
        return currProjectPath;
    }

    /**
     * should get all pom files from spoon model, also from sub modules, important for a maven launcher
     *
     * @param file the file which should be checked for pom occurrences
     */
    private void searchModulesForPom(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                if (f.isDirectory()) {
                    // recursive call of next directory search
                    searchModulesForPom(f);
                } else if (f.getName().toLowerCase().equals("pom.xml") && !f.getAbsolutePath().equals(this.currProjectPath)) {
                    this.pomModels.add(new ImplSpoon(f.getParentFile().getAbsolutePath() + File.separator));
                }

            }
        }
    }

    @Override
    public List<CallNode> getCallNodes() {
        return callNodes;
    }

    @Override
    public void setCallNodes(List<CallNode> callNodes) {
        this.callNodes = callNodes;
    }

    /**
     * function which initializes a new spoon launcher and fills pomModel list with all poms located in Maven-project/jar
     *
     * @param analyzeFromJar whether the launcher will be a JarLauncher of MavenLauncher
     */
    private void initLauncherAndCreatePomModels(boolean analyzeFromJar) {
        if (!analyzeFromJar) {
            this.launcher = new MavenLauncher(this.currProjectPath, MavenLauncher.SOURCE_TYPE.APP_SOURCE); // change source type to all_source to include tests
            this.baseModel = new ImplSpoon(this.currProjectPath);
            searchModulesForPom(new File(currProjectPath));

        } else {
            File jar = new File(this.currProjectPath);
            File pom = new File(this.currProjectPath.replace(".jar", ".pom"));
            if (!jar.exists() || !pom.exists()) {
                System.out.println("Jar and/or pom not found... proceeding with download");
                CentralMavenAPI.downloadMissingFiles(this.currProjectPath);
            }
            this.launcher = new JarLauncher(this.currProjectPath);
            // add new pom model
            this.baseModel = new ImplSpoon(this.currProjectPath);
        }
    }

    /**
     * function which initializes all local class names for current model
     */
    private void initClassNames() {
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

    @Override
    public Map<String, Boolean> computeJarPaths() throws NullPointerException, IOException, InterruptedException, JAXBException {
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


    @Override
    public List<CallNode> analyzeModel(List<Invocation> leafInvocations) {
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
                    searchInvocation(m, s, leafInvocations);
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
     * @param leaves list of current leaf Invocations
     * @return true if method is part of call chain
     */
    private boolean checkMethodFromCallChain(CtMethod method, List<Invocation> leaves) {
        if (leaves == null) return true;
        for (Invocation invocation : leaves) {
            if (invocation.getMethodSignature().split("\\(")[0].equals(method.getSimpleName())
                    && checkJDKClasses(method.getDeclaringType().getQualifiedName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * called by iterateClasses for each method that is part of call chain, than searches for invocations that point to non-local classes and if needed adds them
     * to the call chain of the call tree
     *
     * @param method          current method to analyze
     * @param currClass       String signature of class which current method belongs to
     * @param leafInvocations List of current leaf Invocations
     */
    private void searchInvocation(CtMethod method, CtType currClass, List<Invocation> leafInvocations) throws NullPointerException {
        // get all method body elements
        String currClassName = currClass.getQualifiedName();

        List<CtInvocation> methodCalls = method.getElements(new TypeFilter<>(CtInvocation.class));
        List<CtConstructorCall> constructorCalls = method.filterChildren(new TypeFilter<>(CtConstructorCall.class)).list();
        CallNode currNode = null;
        // creates new Node from and if needed appends it to a leaf
        if (methodCalls.size() != 0 || constructorCalls.size() != 0 || currClass.toString().contains("interface " + currClass.getSimpleName())) {
            currNode = getNodeByName(currClassName, this.currProjectPath);
            if (leafInvocations != null) appendNodeToLeaf(currNode, leafInvocations);
        }
        // adds invocations called by current method to the current CallNode
        for (CtInvocation element : methodCalls) {
            CtTypeReference fromType;
            if (element.getExecutable().getType() == null || element.getExecutable().getType().toString().equals("void") || element.getExecutable().getType().isPrimitive()) {
                fromType = element.getExecutable().getDeclaringType();
            } else {
                fromType = element.getExecutable().getType();
            }
            if (fromType != null && checkJDKClasses(fromType.getQualifiedName())) {
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
     * @param jarPath   String path of jar where class should be located
     * @return {@link CallNode}
     */
    private CallNode getNodeByName(String currClass, String jarPath) {
        for (CallNode n : this.callNodes) {
            if (n.getClassName().equals(currClass) && n.getFromJar().equals(jarPath)) {
                if (n.getPrevious() == null) return n;
            }
        }
        return getCallNode(currClass, jarPath);
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
     * @param currNode        CallNode which corresponds to current Class
     * @param leafInvocations list of invocations from current bottom of call tree
     */
    private void appendNodeToLeaf(CallNode currNode, List<Invocation> leafInvocations) {
        for (Invocation invocation : leafInvocations) {
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
     * @return true if class is not part of JDK
     */
    private boolean checkJDKClasses(String qualifiedName) {
        String[] strings = qualifiedName.split("[.]");
        if (strings.length == 1) return true;
        else return !strings[0].equals("java");
    }


}
