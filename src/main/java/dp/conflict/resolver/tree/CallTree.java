package dp.conflict.resolver.tree;

import java.util.*;

/*********************************
 Created by Fabian Oraze on 03.12.20
 *********************************/

public class CallTree {

    private List<CallNode> startNodes;
    private String targetProjectPath;
    private SpoonModel model;
    private Map<String, Boolean> jars;
    private List<Invocation> currLeafs;

    /**
     * Tree data structure which contains all method call traces from a given root project
     *
     * @param targetProjectPath path to maven project which is to be analyzed
     */
    public CallTree(String targetProjectPath) throws Exception {
        this.targetProjectPath = targetProjectPath;
        this.startNodes = new ArrayList<>();
        this.model = new SpoonModel(targetProjectPath, false);
        this.jars = new HashMap<>();
        this.jars.putAll(this.model.computeJarPaths());
        // compute starting nodes for call tree
        this.startNodes.addAll(this.model.iterateClasses(null));
        this.currLeafs = new ArrayList<>();
        // set current leaf elements
        for (CallNode node : this.startNodes) {
            currLeafs.addAll(node.getInvocations());
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
        this.model.iterateClasses(this.currLeafs);
        computeLeafElements();
        if (jarsToTraverseLeft())
            computeCallTree();
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
        } catch (Exception e) {
            System.err.println("New launcher model could not be built for: " + nextJar);
        }
    }

    /**
     * helper function to compute the current leaf elements of the whole call tree
     * new leaf elements are appended via the next() method from callNodes class to invocation objects
     * old leafs are then removed
     */
    private void computeLeafElements() {
        List<Invocation> toBeRemoved = new ArrayList<>();
        for (int i = 0; i < this.currLeafs.size(); i++) {
            Invocation invocation = this.currLeafs.get(i);
            if (invocation.getNextNode() != null) {
                this.currLeafs.addAll(invocation.getNextNode().getInvocations());
                toBeRemoved.add(invocation);
            }
        }
        this.currLeafs.removeAll(toBeRemoved);
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
