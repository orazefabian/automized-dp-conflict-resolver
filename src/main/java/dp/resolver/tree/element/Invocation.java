package dp.resolver.tree.element;

/*********************************
 Created by Fabian Oraze on 03.12.20
 *********************************/

public class Invocation {

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
}
