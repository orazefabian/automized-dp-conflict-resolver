package dp.resolver.tree;

import dp.resolver.loader.CentralMavenAPI;
import dp.resolver.parse.JarParser;
import dp.resolver.parse.assist.AssistParser;
import dp.resolver.parse.assist.ClazzWithMethodsDto;
import dp.resolver.tree.element.CallNode;
import dp.resolver.tree.element.Invocation;
import spoon.compiler.ModelBuildingException;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.*;

/*********************************
 Created by Fabian Oraze on 03.12.20
 *********************************/

public class CallTree implements Tree {

    private final List<CallNode> startNodes;
    private final String targetProjectPath;
    private AnswerObject answerObject;
    private SpoonModel model;
    private final Map<String, Boolean> jars;
    private final List<Invocation> currLeaves;
    private final List<CallNode> conflicts;
    private final List<String> allUsedJars;

    /**
     * Tree data structure which contains all method call traces from a given root project
     *
     * @param targetProjectPath path to maven project which is to be analyzed, MUST end with a "/" (File separator)
     */
    public CallTree(String targetProjectPath, AnswerObject answerObject) {
        this.targetProjectPath = targetProjectPath;
        this.answerObject = answerObject;
        this.startNodes = new ArrayList<>();
        this.jars = new HashMap<>();
        this.allUsedJars = new ArrayList<>();
        this.conflicts = new ArrayList<>();
        initModel();
        this.currLeaves = new ArrayList<>();
        // set current leaf elements
        for (CallNode node : this.startNodes) {
            currLeaves.addAll(node.getInvocations());
        }
    }

    @Override
    public List<CallNode> getCallTree() {
        return this.startNodes;
    }

    @Override
    public void computeCallTree() {
        createNewModel();
        this.model.analyzeModel(this.currLeaves);
        computeLeafElements();
        if (jarsToTraverseLeft())
            computeCallTree();
    }

    /**
     * helper function which computes the possible conflicts based on a
     * already computed call tree
     *
     * @param type {@link ConflictType} specifies which type of conflict should be computed
     */
    private void computeConflicts(ConflictType type) {
        Set<CallNode> trace = new HashSet<>();
        for (CallNode node : this.startNodes) {
            recursiveSearch(node, trace);
        }
        switch (type) {
            case TYPE_1:
                for (CallNode call : trace) {
                    for (CallNode checkCall : trace) {
                        if (checkForConflictType1(call, checkCall)) {
                            this.conflicts.add(call);
                        }
                    }
                }
                break;
            case TYPE_2:
                for (CallNode call : trace) {
                    for (CallNode checkCall : trace) {
                        if (checkForConflictType2(call, checkCall)) {
                            this.conflicts.add(call);
                        }
                    }
                }
                break;
            case TYPE_3:
                for (CallNode call : trace) {
                    if (checkForConflictType3(call)) {
                        this.conflicts.add(call);
                    }
                }
        }
    }


    /**
     * helper function which checks if a callNode is a leaf node, this type sees all nodes as potential conflicts
     *
     * @param call {@link CallNode}
     * @return true if node is a leaf object of current tree
     */
    private boolean checkForConflictType3(CallNode call) {
        if (call.getPrevious() == null) return false; // do not add root nodes!!
        /*else if (call.getInvocations() == null || call.getInvocations().size() == 0) {
            return true;
        }*/
        else {
            for (Invocation inv : call.getInvocations()) {
                if (inv.getNextNode() != null) return false;
            }
        }
        return true;
    }

    /**
     * helper function which checks if two different CallNodes cause a possible conflict (must have the same fullyQualifiedName and be from different Jars)
     *
     * @param first  {@link CallNode}
     * @param second {@link CallNode}
     * @return true if two calleNodes cause a possible thread
     */
    private boolean checkForConflictType2(CallNode first, CallNode second) {
        return !first.equals(second) && first.getClassName().equals(second.getClassName()) && !first.getFromJar().equals(second.getFromJar());
    }

    /**
     * helper function which checks if two different CallNodes cause a definitive conflict (Objects that use methods in different jars with different method signatures)
     *
     * @param first  {@link CallNode}
     * @param second {@link CallNode}
     * @return true if they definitely cause an error
     */
    private boolean checkForConflictType1(CallNode first, CallNode second) {
        boolean isOfTypeOne = !first.equals(second) && first.getClassName().equals(second.getClassName()) && !first.getFromJar().equals(second.getFromJar());
        if (!isOfTypeOne) return false;
        for (Invocation invFirst : first.getPrevious().getInvocations()) {
            for (Invocation invSec : second.getPrevious().getInvocations()) {
                // get method names without parameters
                String method1 = invFirst.getMethodSignature().split("\\(")[0];
                String method2 = invSec.getMethodSignature().split("\\(")[0];
                // check if method signatures differ
                if (method1.equals(method2)
                        && invFirst.getDeclaringType().equals(invSec.getDeclaringType())
                        && !invFirst.getMethodSignature().equals(invSec.getMethodSignature())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<CallNode> getConflicts(ConflictType type) {
        computeConflicts(type);
        return this.conflicts;
    }

    /**
     * function that recursively fills a set with all nodes from the given root node
     *
     * @param callNode the root node
     * @param trace    the set with call nodes that is being filled
     */
    private void recursiveSearch(CallNode callNode, Set<CallNode> trace) {
        trace.add(callNode);
        try {
            for (Invocation inv : callNode.getInvocations()) {
                if (inv.getNextNode() != null) recursiveSearch(inv.getNextNode(), trace);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * initialize first spoon model from a maven launcher for a targetProject
     */
    private void initModel() {
        // compute starting nodes for call tree
        try {
            this.model = new SpoonModel(targetProjectPath, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.jars.putAll(this.model.computeJarPaths());
        } catch (IOException | InterruptedException | JAXBException e) {
            e.printStackTrace();
        }
        this.startNodes.addAll(this.model.analyzeModel(null));
    }

    /**
     * helper function to create new {@link SpoonModel} for next jar, after analyzing previous one
     * also removes unused/bloated jars
     */
    private void createNewModel() {
        List<CallNode> prevCallNodes = this.model.getCallNodes();
        List<String> jarsToRemove = new ArrayList<>();
        for (String jarPath : this.jars.keySet()) {
            // remove non used jars
            checkIfJarExists(jarPath);
            if (checkIfJarUsed(jarPath)) jarsToRemove.add(jarPath);
        }
        for (String key : jarsToRemove) {
            this.jars.remove(key);
            if (this.model.getCurrProjectPath().equals(this.targetProjectPath))
                this.answerObject.addBloatedJar(key); // add jars that are directly bloated (root pom)
        }
        String nextJar = getNonTraversedJar();
        // save already traversed jars for later conflict search
        this.allUsedJars.add(nextJar);
        try {
            this.model = new SpoonModel(nextJar, true);
            this.model.setCallNodes(prevCallNodes);
            this.jars.putAll(this.model.computeJarPaths());
        } catch (ModelBuildingException e) {
            System.err.println("Error building models: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("No Dependencies found for given project");
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("New launcher model could not be built for: " + nextJar);
            e.printStackTrace();
        }

    }

    /**
     * additional check if jar is not existent, it should be downloaded with the spoonModel
     *
     * @param nextJar jar to be checked if existent
     */
    private void checkIfJarExists(String nextJar) {
        File jar = new File(nextJar);
        File pom = new File(nextJar.replace(".jar", ".pom"));
        if (!jar.exists() || !pom.exists()) {
            System.out.println("Jar and/or pom not found... proceeding with download");
            CentralMavenAPI.downloadMissingFiles(nextJar);
        }
    }

    /**
     * helper function to compute the current leaf elements of the whole call tree
     * new leaf elements are appended via the next() method from callNodes class to invocation objects
     * old leaves are then removed
     */
    private void computeLeafElements() {
        List<Invocation> toBeRemoved = new ArrayList<>();
        for (int i = 0; i < this.currLeaves.size(); i++) {
            Invocation invocation = this.currLeaves.get(i);
            if (invocation.getNextNode() != null) {
                this.currLeaves.addAll(invocation.getNextNode().getInvocations());
                toBeRemoved.add(invocation);
            }
        }
        this.currLeaves.removeAll(toBeRemoved);
    }

    /**
     * helper function to determine if jars which still need to be traversed are left
     *
     * @return true if there are jars left otherwise false
     */
    private boolean jarsToTraverseLeft() {
        for (Boolean traversed : this.jars.values()) {
            if (!traversed) {
                return true;
            }
        }
        return false;
    }

    /**
     * helper function to get the next jar to be analyzed
     *
     * @return String representation of a jar
     */
    private String getNonTraversedJar() {
        for (String path : this.jars.keySet()) {
            if (!this.jars.get(path)) {
                this.jars.put(path, true);
                return path;
            }
        }
        return null;
    }

    /**
     * function that checks if a given jar is used by any call of the current invocations
     *
     * @param jarPath String representation of the complete path to the Jar to be checked for usage
     * @return true if the given jar is not used
     */
    private boolean checkIfJarUsed(String jarPath) {
        List<ClazzWithMethodsDto> jarClassList = AssistParser.getJarClassList(jarPath);
        boolean remove = true;
        for (Invocation invocation : this.currLeaves) {
            try {
                for (ClazzWithMethodsDto clazz : jarClassList) {
                    if (clazz.getClazzName().replace(".class", "").replace(File.separator, ".").equals(invocation.getDeclaringType())) {
                        remove = false;
                        break;
                    }
                }
            } catch (Exception e) {
                // skip to next inovcation
            }
            if (!remove) break;
        }

        return remove;
    }


}
