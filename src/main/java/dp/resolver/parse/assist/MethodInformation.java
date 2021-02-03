package dp.resolver.parse.assist;

import java.util.Objects;

/*********************************
 Created by Fabian Oraze on 03.02.21
 *********************************/

public class MethodInformation {
    private String retType;
    private Boolean hasPrimitiveReturnType;
    private Boolean hasVoidReturnType;

    private Boolean isAbstract;
    private Boolean isFinal;
    private Boolean isSynchronized;
    private Boolean isStatic;
    private String methodName;
    private String methodHeader;
    private Long numberOfParams;
    private String visibility;

    private Boolean isInformationComplete;

    private Boolean deprecated;

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

    public Boolean getInformationComplete() {
        return isInformationComplete;
    }

    public void setInformationComplete(Boolean informationComplete) {
        isInformationComplete = informationComplete;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodInformation that = (MethodInformation) o;
        return Objects.equals(retType, that.retType) &&
                Objects.equals(hasPrimitiveReturnType, that.hasPrimitiveReturnType) &&
                Objects.equals(hasVoidReturnType, that.hasVoidReturnType) &&
                Objects.equals(isAbstract, that.isAbstract) &&
                Objects.equals(isFinal, that.isFinal) &&
                Objects.equals(isSynchronized, that.isSynchronized) &&
                Objects.equals(isStatic, that.isStatic) &&
                Objects.equals(methodName, that.methodName) &&
                Objects.equals(methodHeader, that.methodHeader) &&
                Objects.equals(numberOfParams, that.numberOfParams) &&
                Objects.equals(visibility, that.visibility) &&
                Objects.equals(isInformationComplete, that.isInformationComplete) &&
                Objects.equals(deprecated, that.deprecated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(retType, hasPrimitiveReturnType, hasVoidReturnType, isAbstract, isFinal, isSynchronized, isStatic, methodName, methodHeader, numberOfParams, visibility, isInformationComplete, deprecated);
    }

    @Override
    public String toString() {
        return "MethodInformation{" +
                "retType='" + retType + '\'' +
                ", hasPrimitiveReturnType=" + hasPrimitiveReturnType +
                ", hasVoidReturnType=" + hasVoidReturnType +
                ", isAbstract=" + isAbstract +
                ", isFinal=" + isFinal +
                ", isSynchronized=" + isSynchronized +
                ", isStatic=" + isStatic +
                ", methodName='" + methodName + '\'' +
                ", methodHeader='" + methodHeader + '\'' +
                ", numberOfParams=" + numberOfParams +
                ", visibility='" + visibility + '\'' +
                ", isInformationComplete=" + isInformationComplete +
                ", deprecated=" + deprecated +
                '}';
    }
}
