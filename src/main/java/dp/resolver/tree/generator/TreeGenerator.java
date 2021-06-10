package dp.resolver.tree.generator;

import dp.resolver.tree.CallTree;
import dp.resolver.tree.element.CallNode;

import java.util.List;
import java.util.Set;

/*********************************
 Created by Fabian Oraze on 03.02.21
 *********************************/

public interface TreeGenerator {
    /**
     * @return complete call tree
     */
    CallTree getCallTree();

    /**
     * method which computes the complete call trace for all method invocations from
     * the root project pointing to other dependencies recursively
     */
    void computeCallTree();

    /**
     * computes the conflicts if called the first time
     *
     * @param type {@link ConflictType} enumeration of the type of conflict that should be determined
     * @return {@link List<CallNode>} which cause an issue
     */
    List<CallNode> getConflicts(ConflictType type);

    /**
     * @return set of needed jars that could not be fully analyzed
     */
    Set getNeededJars();
}
