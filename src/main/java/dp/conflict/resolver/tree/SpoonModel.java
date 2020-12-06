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

    public List<CallNode> getCallNodes() {
        return callNodes;
    }

    public void setCallNodes(List<CallNode> callNodes) {
        this.callNodes = callNodes;
    }

    private void initLauncher(boolean analyzeFromJar) {
        if (!analyzeFromJar) {
            this.launcher = new MavenLauncher(this.currProjectPath, MavenLauncher.SOURCE_TYPE.ALL_SOURCE);
        } else {
            this.launcher = new JarLauncher(this.currProjectPath);
        }
    }

    private void initClassNames() {
        this.classNames.clear();
        for (CtType<?> c : this.ctModel.getAllTypes()) this.classNames.add(c.getSimpleName());
    }

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

    public List<CallNode> iterateClasses(List<Invocation> leafInvocations) {
        // iterate over all classes in model
        for (CtType<?> s : this.ctModel.getAllTypes()) {
            try {
                for (CtMethod<?> m : s.getAllMethods()) {
                    searchInvocation(m, s.getSimpleName(), leafInvocations);
                }
            } catch (SpoonException e) {
                System.err.println("could not iterate over methods in class: " + s.getSimpleName());
            }
        }

        return this.callNodes;
    }


    private void searchInvocation(CtMethod method, String currClass, List<Invocation> leafInvocations) {
        // get all method body elements
        List<CtInvocation> elements = method.getElements(new TypeFilter<>(CtInvocation.class));
        if (elements.size() != 0) this.callNodes.add(new CallNode(currClass, currProjectPath, this.jarPaths.keySet()));
        CallNode currNode = getNodeByName(currClass);
        if (leafInvocations != null && currNode != null) appendNodeToLeaf(currNode, leafInvocations);
        for (CtInvocation element : elements) {
            CtTypeReference declaringType = element.getExecutable().getDeclaringType();
            if (declaringType != null && checkJDKClasses(declaringType.getQualifiedName()) && !this.classNames.contains(declaringType.getSimpleName())) {
                String methodSignature = element.getExecutable().getSimpleName();
                currNode.addInvocation(new Invocation(methodSignature, declaringType.toString(), currNode));
            }
        }
    }

    private void appendNodeToLeaf(CallNode currNode, List<Invocation> leafInvocations) {
        for (Invocation invocation : leafInvocations) {
            if (invocation.getDeclaringType().equals(currNode.getClassName())
                    && invocation.getParentNode().getCurrPomJarDependencies().contains(currNode.getFromJar()))
                invocation.setNextNode(currNode);
        }
    }

    private CallNode getNodeByName(String currClass) {
        for (CallNode n : this.callNodes) {
            if (n.getClassName().equals(currClass)) return n;
        }
        return null;
    }

    private boolean checkJDKClasses(String qualifiedName) {
        String[] strings = qualifiedName.split("[.]");
        if (strings.length == 1) return true;
        else return !strings[0].equals("java");
    }


}
