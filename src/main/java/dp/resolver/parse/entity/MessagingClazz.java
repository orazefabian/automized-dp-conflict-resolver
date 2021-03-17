package dp.resolver.parse.entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessagingClazz implements Serializable {
    private String fullQualifiedName;

    private String packageName;

    private String clazzName;

    private List<MessagingMethod> methods=new ArrayList<MessagingMethod>();

    private Boolean isDeprecated;

    public String getFullQualifiedName() {
        return fullQualifiedName;
    }

    public void setFullQualifiedName(String fullQualifiedName) {
        this.fullQualifiedName = fullQualifiedName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public List<MessagingMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<MessagingMethod> methods) {
        this.methods = methods;
    }

    public Boolean getDeprecated() {
        return isDeprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        isDeprecated = deprecated;
    }
}