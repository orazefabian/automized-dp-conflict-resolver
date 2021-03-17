package dp.resolver.parse.entity;


import java.io.Serializable;

public class MessagingMethod implements Serializable {
    private String retType;
    private Boolean hasPrimitiveReturnType;
    private Boolean hasVoidReturnType;

    private Boolean isAbstract;
    private Boolean isFinal;
    private Boolean isSynchronized;
    private Boolean isStatic;
    private Boolean isDeprecated;

    private String methodName;
    private String methodHeader;
    private Long numberOfParams;
    private String visibility;


    public String getRetType() {
        return retType;
    }

    public void setRetType(String retType) {
        this.retType = retType;
    }

    public Boolean getHasPrimitiveReturnType() {
        return hasPrimitiveReturnType;
    }

    public void setHasPrimitiveReturnType(Boolean hasPrimitiveReturnType) {
        this.hasPrimitiveReturnType = hasPrimitiveReturnType;
    }

    public Boolean getHasVoidReturnType() {
        return hasVoidReturnType;
    }

    public void setHasVoidReturnType(Boolean hasVoidReturnType) {
        this.hasVoidReturnType = hasVoidReturnType;
    }

    public Boolean getAbstract() {
        return isAbstract;
    }

    public void setAbstract(Boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public Boolean getFinal() {
        return isFinal;
    }

    public void setFinal(Boolean aFinal) {
        isFinal = aFinal;
    }

    public Boolean getSynchronized() {
        return isSynchronized;
    }

    public void setSynchronized(Boolean aSynchronized) {
        isSynchronized = aSynchronized;
    }

    public Boolean getStatic() {
        return isStatic;
    }

    public void setStatic(Boolean aStatic) {
        isStatic = aStatic;
    }

    public Boolean getDeprecated() {
        return isDeprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        isDeprecated = deprecated;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodHeader() {
        return methodHeader;
    }

    public void setMethodHeader(String methodHeader) {
        this.methodHeader = methodHeader;
    }

    public Long getNumberOfParams() {
        return numberOfParams;
    }

    public void setNumberOfParams(Long numberOfParams) {
        this.numberOfParams = numberOfParams;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}
