package dp.resolver.parse.entity;

/*********************************
 Created by Fabian Oraze on 03.02.21
 *********************************/

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@SequenceGenerator(name = "methodIdSequence", initialValue = 1, allocationSize = 1, sequenceName="methodIdSequence")
public class Method {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "methodIdSequence")
    private Long id;

    private Long clazzId;

    @Column(columnDefinition = "TEXT")
    private String retType;
    private Boolean hasPrimitiveReturnType;
    private Boolean hasVoidReturnType;

    private Boolean isAbstract;
    private Boolean isFinal;
    private Boolean isSynchronized;
    private Boolean isStatic;
    private Boolean isDeprecated;

    @Column(columnDefinition = "TEXT")
    private String methodName;
    @Column(columnDefinition = "TEXT")
    private String methodHeader;
    private Long numberOfParams;
    @Column(columnDefinition = "TEXT")
    private String visibility;



    public Method(){}
    public Method(Long clazzId, String retType, Boolean hasPrimitiveReturnType, Boolean hasVoidReturnType, Boolean isAbstract, Boolean isFinal, Boolean isSynchronized, Boolean isStatic, String methodName, String methodHeader, Long numberOfParams, String visibility, Boolean isDeprecated) {
        this.clazzId = clazzId;
        this.retType = retType;
        this.hasPrimitiveReturnType = hasPrimitiveReturnType;
        this.hasVoidReturnType = hasVoidReturnType;
        this.isAbstract = isAbstract;
        this.isFinal = isFinal;
        this.isSynchronized = isSynchronized;
        this.isStatic = isStatic;
        this.methodName = methodName;
        this.methodHeader = methodHeader;
        this.numberOfParams = numberOfParams;
        this.visibility = visibility;
        this.isDeprecated = isDeprecated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClazzId() {
        return clazzId;
    }

    public void setClazzId(Long clazzId) {
        this.clazzId = clazzId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Method method = (Method) o;
        return Objects.equals(id, method.id) &&
                Objects.equals(clazzId, method.clazzId) &&
                Objects.equals(retType, method.retType) &&
                Objects.equals(hasPrimitiveReturnType, method.hasPrimitiveReturnType) &&
                Objects.equals(hasVoidReturnType, method.hasVoidReturnType) &&
                Objects.equals(isAbstract, method.isAbstract) &&
                Objects.equals(isFinal, method.isFinal) &&
                Objects.equals(isSynchronized, method.isSynchronized) &&
                Objects.equals(isStatic, method.isStatic) &&
                Objects.equals(isDeprecated, method.isDeprecated) &&
                Objects.equals(methodName, method.methodName) &&
                Objects.equals(methodHeader, method.methodHeader) &&
                Objects.equals(numberOfParams, method.numberOfParams) &&
                Objects.equals(visibility, method.visibility);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clazzId, retType, hasPrimitiveReturnType, hasVoidReturnType, isAbstract, isFinal, isSynchronized, isStatic, isDeprecated, methodName, methodHeader, numberOfParams, visibility);
    }

    @Override
    public String toString() {
        return "Method{" +
                "id=" + id +
                ", clazzId=" + clazzId +
                ", retType='" + retType + '\'' +
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
                ", isDeprecated='" + isDeprecated + '\'' +
                '}';
    }

    public Boolean getDeprecated() {
        return isDeprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        isDeprecated = deprecated;
    }
}
