package dp.resolver.tree.model.entity;

/*********************************
 Created by Fabian Oraze on 19.03.21
 *********************************/

public class MethodConnection {

    private String fromClass;
    private String toMethodSignature;
    private String toClass;

    public MethodConnection(String fromClass, String toMethodSignature, String toClass) {
        this.fromClass = fromClass;
        this.toMethodSignature = toMethodSignature;
        this.toClass = toClass;
    }

    public String getFromClass() {
        return fromClass;
    }

    public void setFromClass(String fromClass) {
        this.fromClass = fromClass;
    }

    public String getToMethodSignature() {
        return toMethodSignature;
    }

    public void setToMethodSignature(String toMethodSignature) {
        this.toMethodSignature = toMethodSignature;
    }

    public String getToClass() {
        return toClass;
    }

    public void setToClass(String toClass) {
        this.toClass = toClass;
    }

    @Override
    public boolean equals(Object obj) {
        MethodConnection compare = (MethodConnection) obj;
        return compare.getFromClass().equals(this.fromClass) && compare.getToClass().equals(this.toClass) && compare.getToMethodSignature().equals(this.toMethodSignature);
    }
}
