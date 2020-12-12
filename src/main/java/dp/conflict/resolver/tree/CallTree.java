package dp.conflict.resolver.tree;

import spoon.compiler.ModelBuildingException;

import java.io.IOException;
import java.util.*;

/*********************************
 Created by Fabian Oraze on 03.12.20
 *********************************/

public class CallTree {

    private List<CallNode> startNodes;
    private String targetProjectPath;
    private SpoonModel model;
    private Map<String, Boolean> jars;
    private List<Invocation> currLeaves;
    private List<CallNode> conflicts;

    /**
     * Tree data structure which contains all method call traces from a given root project
     *
     * @param targetProjectPath path to maven project which is to be analyzed
     */
    public CallTree(String targetProjectPath) throws Exception {
        this.targetProjectPath = targetProjectPath;
        this.startNodes = new ArrayList<>();
        this.jars = new HashMap<>();
        this.conflicts = new ArrayList<>();
        initModel();
        this.currLeaves = new ArrayList<>();
        // set current leaf elements
        for (CallNode node : this.startNodes) {
            currLeaves.addAll(node.getInvocations());
        }
    }

    /**
     * @return complete call tree
     */
    public List<CallNode> getCallTree() {
        return this.startNodes;
    }

    /**
     * method which computes the complete call trace for all method invocations from
     * the root project pointing to other dependencies recursively
     */
    public void computeCallTree() {
        createNewModel();
        this.model.iterateClasses(this.currLeaves);
        computeLeafElements();
        if (jarsToTraverseLeft())
            computeCallTree();
    }

    /**
     * helper function which computes the possible conflicts based on a
     * already computed call tree
     */
    private void computeConflicts() {
        List<CallNode> trace = new ArrayList<>();
        for (CallNode node : this.startNodes) {
            recursiveSearch(node, trace);
        }
        for (CallNode call : trace) {
            for (CallNode checkCall : trace) {
                if (checkForConflict(call, checkCall)) {
                    this.conflicts.add(call);
                }
            }
        }
    }

    /**
     * helper function which checks if two different CallNodes cause a possible conflict (must have the same fullyQualifiedName and be from different Jars)
     * @param first {@link CallNode}
     * @param second {@link CallNode}
     * @return true if two calleNodes cause a possible thread
     */
    private boolean checkForConflict(CallNode first, CallNode second) {
        if(!first.equals(second) && first.getClassName().equals(second.getClassName()) && !first.getFromJar().equals(second.getFromJar())) {
            return true;
        }
        return false;
    }

    /**
     * computes the conflicts if called the first time
     * @return {@link List<CallNode>} which cause an issue
     */
    public List<CallNode> getConflicts() {
        if (this.conflicts.size() == 0) {
            computeConflicts();
        }
        return conflicts;
    }

    private void recursiveSearch(CallNode callNode, List<CallNode> trace) {
        trace.add(callNode);
        for (Invocation inv : callNode.getInvocations()) {
            recursiveSearch(inv.getNextNode(), trace);
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
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        this.startNodes.addAll(this.model.iterateClasses(null));
    }

    /**
     * helper function to create new {@link SpoonModel} for next jar
     */
    private void createNewModel() {
        List<CallNode> prevCallNodes = this.model.getCallNodes();
        String nextJar = getNonTraversedJar();
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
     * helper function to compute the current leaf elements of the whole call tree
     * new leaf elements are appended via the next() method from callNodes class to invocation objects
     * old leafs are then removed
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
            if (!traversed) return true;
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


}
