package dp.resolver.tree;

import dp.resolver.tree.element.CallNode;
import dp.resolver.tree.element.Invocation;

import java.util.*;

public class CallTreeImpl implements CallTree {

    private List<CallNode> rootNodes;
    private Set<Invocation> leafInvocations;

    public CallTreeImpl() {
        this.rootNodes = new ArrayList<>();
        this.leafInvocations = new HashSet<>();
    }

    @Override
    public List<CallNode> getRootNodes() {
        return this.rootNodes;
    }

    @Override
    public void addNodes(Collection<CallNode> nodes) {
        this.rootNodes.addAll(nodes);
    }

    @Override
    public Set<Invocation> getCurrLeaves() {
        return this.leafInvocations;
    }

    @Override
    public void addLeaves(Collection<Invocation> leaves) {
        this.leafInvocations.addAll(leaves);
    }

    @Override
    public void removeLeaves(Collection<Invocation> leaves) {
        this.leafInvocations.removeAll(leaves);
    }

    /**
     * helper function to compute the current leaf elements of the whole call tree
     * new leaf elements are appended via the next() method from callNodes class to invocation objects
     * old leaves are then removed
     */
    @Override
    public void computeLeafElements() {
        List<Invocation> toBeRemoved = new ArrayList<>();
        List<Invocation> toBeAdded = new ArrayList<>();
        for (Invocation invocation : getCurrLeaves()) {
            if (!invocation.isLeafInvocation()) {
                toBeAdded.addAll(invocation.getNextNode().getInvocations());
                toBeRemoved.add(invocation);
            }
        }
        addLeaves(toBeAdded);
        removeLeaves(toBeRemoved);
    }
}
