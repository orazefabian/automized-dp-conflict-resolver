package dp.resolver.tree;

import dp.resolver.tree.element.CallNode;
import dp.resolver.tree.element.Invocation;
import dp.resolver.tree.generator.TreeGeneratorImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CallTree {

    List<CallNode> getRootNodes();

    void addNodes(Collection<CallNode> nodes);

    Set<Invocation> getCurrLeaves();

    void addLeaves(Collection<Invocation> leaves);

    void removeLeaves(Collection<Invocation> leaves);

    void computeLeafElements();
}
