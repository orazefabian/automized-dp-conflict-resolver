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
}
