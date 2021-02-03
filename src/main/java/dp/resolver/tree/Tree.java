package dp.resolver.tree;

import dp.resolver.tree.element.CallNode;

import java.util.List;

/*********************************
 Created by Fabian Oraze on 03.02.21
 *********************************/

public interface Tree {
    /**
     * @return complete call tree
     */
    List<CallNode> getCallTree();

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
}
