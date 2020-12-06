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
        this.startNodes.addAll(this.model.iterateMethods(null));
        this.currLeafs = new ArrayList<>();
        // set current leaf elements
        for (CallNode node : this.startNodes) {
            currLeafs.addAll(node.getInvocations());
        }
    }

    public List<CallNode> getCallTree() {
        return this.startNodes;
    }

    /**
     * method which computes the complete call trace for all method invocations from
     * the root project pointing to other dependencies recursively
     */
    public void computeCallTree() {

        createNewModel();
        this.model.iterateMethods(this.currLeafs);
        computeLeafElements();
        if (jarsToTraverseLeft())
            computeCallTree();
    }

    private void createNewModel() {
        List<CallNode> prevCallNodes = this.model.getCallNodes();
        try {
            this.model = new SpoonModel(getNonTraversedJar(), true);
            this.model.setCallNodes(prevCallNodes);
            this.jars.putAll(this.model.computeJarPaths());
        } catch (Exception e) {
            System.err.println("New launcher model could not be built");
        }
    }

    private void computeLeafElements() {
        for (Invocation invocation : this.currLeafs) {
            if (invocation.getNextNode() != null) {
                this.currLeafs.addAll(invocation.getNextNode().getInvocations());
                this.currLeafs.remove(invocation);
                //TODO: make loop not break, but not cause error because list gets updated dynamically!
                break;
            }
        }
    }

    private boolean jarsToTraverseLeft() {
        for (Boolean traversed : this.jars.values()) {
            if (!traversed) return true;
        }
        return false;
    }

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
