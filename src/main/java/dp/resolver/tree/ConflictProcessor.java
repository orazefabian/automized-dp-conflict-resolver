package dp.resolver.tree;

import dp.resolver.tree.element.CallNode;
import dp.resolver.tree.element.Invocation;

public class ConflictProcessor {

    /**
     * helper function which checks if a callNode is a leaf node, this type sees all nodes as potential conflicts
     *
     * @param call {@link CallNode}
     * @return true if node is a leaf object of current tree
     */
    public static boolean checkForConflictType3(CallNode call) {
        if (call.getPrevious() == null) return false; // do not add root nodes
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
    //TODO: adapt and verify
    public static boolean checkForConflictType2(CallNode first, CallNode second) {
        return !first.equals(second) && first.getClassName().equals(second.getClassName()) && !first.getFromJar().equals(second.getFromJar());
    }

    /**
     * helper function which checks if two different CallNodes cause a definitive conflict (Objects that use methods in different jars with different method signatures)
     *
     * @param first  {@link CallNode}
     * @param second {@link CallNode}
     * @return true if they definitely cause an error
     */
    //TODO: adapt and verify
    public static boolean checkForConflictType1(CallNode first, CallNode second) {
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
}