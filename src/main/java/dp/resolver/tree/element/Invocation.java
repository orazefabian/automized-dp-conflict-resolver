package dp.resolver.tree.element;

import org.jetbrains.annotations.NotNull;

/*********************************
 Created by Fabian Oraze on 03.12.20
 *********************************/

public class Invocation implements Comparable<Invocation> {

    private String methodSignature;
    private String declaringType;
    private CallNode parentNode;
    private CallNode nextNode;

    public Invocation(String methodSignature, String declaringType, CallNode parentNode) {
        this.setMethodSignature(methodSignature);
        this.setDeclaringType(declaringType);
        this.setParentNode(parentNode);
        this.nextNode = null;
    }

    public String getDeclaringType() {
        return declaringType;
    }

    public void setDeclaringType(String declaringType) {
        this.declaringType = declaringType.replace("[]", "");
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public void setMethodSignature(String methodSignature) {
        this.methodSignature = methodSignature;
    }

    public CallNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(CallNode parentNode) {
        this.parentNode = parentNode;
    }

    public CallNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(CallNode nextNode) {
        this.nextNode = nextNode;
    }

    public boolean isLeafInvocation() {
        return this.nextNode == null;
    }

    @Override
    public int compareTo(@NotNull Invocation o) {
        String compMethodSignature = o.getMethodSignature();
        return compMethodSignature.compareTo(this.methodSignature);
    }

    @Override
    public boolean equals(Object obj) {
        Invocation compare = (Invocation) obj;
        return compare.getDeclaringType().equals(this.declaringType)
                && compare.getMethodSignature().equals(this.methodSignature)
                && compare.getParentNode().equals(this.parentNode);
    }
}
