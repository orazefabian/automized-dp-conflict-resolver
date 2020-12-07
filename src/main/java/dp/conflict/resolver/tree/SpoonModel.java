package dp.conflict.resolver.tree;

import dp.conflict.resolver.base.DPUpdaterBase;
import dp.conflict.resolver.base.ImplSpoon;
import org.apache.maven.pom._4_0.Dependency;
import spoon.JarLauncher;
import spoon.Launcher;
import spoon.MavenLauncher;
import spoon.SpoonException;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.File;
import java.util.*;

/*********************************
 Created by Fabian Oraze on 27.11.20
 *********************************/

public class SpoonModel {

    private DPUpdaterBase base;
    private Launcher launcher;
    private CtModel ctModel;
    private String pathM2;
    private List<String> classNames;
    private Map<String, Boolean> jarPaths;
    private HashSet<String> alreadyInvokedMethods;
    private String currProjectPath;
    private List<CallNode> callNodes;


    /**
     * object which builds a new spoon launcher which provides a AST
     * @param pathToProject String to a project, can be maven root folder or path to .jar file
     * @param analyzeFromJar boolean whether the pathToProject is a .jar file
     * @throws Exception if building the spoon model fails
     */
    public SpoonModel(String pathToProject, boolean analyzeFromJar) throws Exception {
        this.currProjectPath = pathToProject;
        setPathM2();
        initLauncher(analyzeFromJar);
        this.classNames = new ArrayList<>();
        this.jarPaths = new HashMap<>();
        this.alreadyInvokedMethods = new HashSet<>();
        this.ctModel = this.launcher.buildModel();
        this.base = new ImplSpoon(pathToProject, this.pathM2);
        callNodes = new ArrayList<>();
        initClassNames();
        computeJarPaths();
    }

    /**
     * @return list of CallNodes of the current model
     */
    public List<CallNode> getCallNodes() {
        return callNodes;
    }

    /**
     * set the available CallNodes for current model
     * @param callNodes
     */
    public void setCallNodes(List<CallNode> callNodes) {
        this.callNodes = callNodes;
    }

    /**
     * function which initializes a new spoon launcher
     * @param analyzeFromJar whether the launcher will be a JarLauncher of MavenLauncher
     */
    private void initLauncher(boolean analyzeFromJar) {
        if (!analyzeFromJar) {
            this.launcher = new MavenLauncher(this.currProjectPath, MavenLauncher.SOURCE_TYPE.ALL_SOURCE);
        } else {
            this.launcher = new JarLauncher(this.currProjectPath);
        }
    }

    /**
     * function which initializes all local class names for current model
     */
    private void initClassNames() {
        this.classNames.clear();
        for (CtType<?> c : this.ctModel.getAllTypes()) this.classNames.add(c.getSimpleName());
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
     * compute jar paths for all dependencies of the current spoon model
     * @return HashMap with String pathsToJar as keys and a initial boolean value false
     */
    public Map<String, Boolean> computeJarPaths() {
        this.jarPaths.clear();
        try {
            for (Dependency dp : this.base.getPomModel().getDependencies().getDependency()) {
                if (dp.toString().contains("${")) {
                    File directoryPath = new File(this.pathM2 + (dp.getGroupId() + "." + dp.getArtifactId()).replace('.', '/'));
                    String versions[] = directoryPath.list();
                    dp.setVersion(versions[0]);
                }
                String postFixJar = dp.getArtifactId() + "-" + dp.getVersion() + ".jar";
                String currPath = "";
                if (System.getProperty("os.name").startsWith("Windows")) {
                    currPath = this.pathM2 + (dp.getGroupId() + "." + dp.getArtifactId()).replace('.', '\\') + "\\" + dp.getVersion() + "\\" + postFixJar;
                } else {
                    currPath = this.pathM2 + (dp.getGroupId() + "." + dp.getArtifactId()).replace('.', '/') + "/" + dp.getVersion() + "/" + postFixJar;
                }

                jarPaths.put(currPath, false);
            }
            return this.jarPaths;
        } catch (NullPointerException e) {
            System.out.println("No Dependencies found for given project");
            return null;
        }
    }


    /**
     * function that iterates over all methods of all classes of the current spoon model and analyzes the invocations of
     * used methods
     * @param leafInvocations a list of current leafInvocations that represent the bottom of the current call tree
     * @return list of current used CallNodes
     */
    public List<CallNode> iterateClasses(List<Invocation> leafInvocations) {
        // iterate over all classes in model
        for (CtType<?> s : this.ctModel.getAllTypes()) {
            try {
                for (CtMethod<?> m : s.getAllMethods()) {
                    if (checkMethodFromCallChain(m, leafInvocations)) {
                        searchInvocation(m, s.getQualifiedName(), leafInvocations);
                    }
                }
            } catch (SpoonException e) {
                System.err.println("could not iterate over methods in class: " + s.getSimpleName());
            }
        }
        return this.callNodes;
    }

    /**
     * helper function that check if a method is part of current call chain
     * @param method currently iterated method
     * @param leafs list of current leaf Invocations
     * @return true if method is part of call chain
     */
    private boolean checkMethodFromCallChain(CtMethod method, List<Invocation> leafs) {
        if (leafs == null) return true;
        for (Invocation invocation : leafs) {
            if (invocation.getMethodSignature().equals(method.getSignature())) return true;
        }
        return false;
    }

    /**
     * called by iterateClasses for each method that is part of call chain, than searches for invocations that point to non-local classes and if needed adds them
     * to the call chain of the call tree
     * @param method current method to analyze
     * @param currClass String signature of class which current method belongs to
     * @param leafInvocations List of current leaf Invocations
     */
    private void searchInvocation(CtMethod method, String currClass, List<Invocation> leafInvocations) {
        // get all method body elements
        List<CtInvocation> elements = method.getElements(new TypeFilter<>(CtInvocation.class));
        CallNode currNode = null;
        if (elements.size() != 0) {
            currNode = getNodeByName(currClass, this.currProjectPath);
            if (leafInvocations != null && currNode != null) appendNodeToLeaf(currNode, leafInvocations);
        }
        for (CtInvocation element : elements) {
            CtTypeReference declaringType = element.getExecutable().getDeclaringType();
            if (declaringType != null && checkJDKClasses(declaringType.getQualifiedName()) && !this.classNames.contains(declaringType.getSimpleName())) {
                String methodSignature = element.getExecutable().toString();
                currNode.addInvocation(new Invocation(methodSignature, declaringType.toString(), currNode));
            }
        }
    }

    /**
     * helper function that gets a CallNode from local list of callNodes
     * @param currClass String name of current class
     * @param jarPath String path of jar where class should be located
     * @return {@link CallNode}
     */
    private CallNode getNodeByName(String currClass, String jarPath) {
        for (CallNode n : this.callNodes) {
            if (n.getClassName().equals(currClass) && n.getFromJar().equals(jarPath)) return n;
        }
        CallNode currNode = new CallNode(currClass, this.currProjectPath, this.jarPaths.keySet());
        this.callNodes.add(currNode);
        return currNode;
    }

    /**
     * helper function to append a CallNode to the correct Invocation from the leaf elements
     * @param currNode CallNode which corresponds to current Class
     * @param leafInvocations list of invocations from current bottom of call tree
     */
    private void appendNodeToLeaf(CallNode currNode, List<Invocation> leafInvocations) {
        for (Invocation invocation : leafInvocations) {
            if (invocation.getDeclaringType().equals(currNode.getClassName())
                    && invocation.getParentNode().getCurrPomJarDependencies().contains(currNode.getFromJar()))
                invocation.setNextNode(currNode);
        }
    }

    /**
     * helper function which checks if a class is part of the JDK
     * @param qualifiedName String name of class
     * @return true if class is not part of JDK
     */
    private boolean checkJDKClasses(String qualifiedName) {
        String[] strings = qualifiedName.split("[.]");
        if (strings.length == 1) return true;
        else return !strings[0].equals("java");
    }


}
