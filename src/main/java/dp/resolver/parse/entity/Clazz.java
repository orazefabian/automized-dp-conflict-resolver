package dp.resolver.parse.entity;

/*********************************
 Created by Fabian Oraze on 03.02.21
 *********************************/

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@SequenceGenerator(name = "clazzIdSequence", initialValue = 1, allocationSize = 1, sequenceName="clazzIdSequence")
public class Clazz implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clazzIdSequence")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String fullQualifiedName;

    private Long jarId;

    @Column(columnDefinition = "TEXT")
    private String packageName;

    @Column(columnDefinition = "TEXT")
    private String clazzName;

    @OneToMany(mappedBy="clazzId")
    private List<Method> methods=new ArrayList<Method>();

    private Boolean isDeprecated;

    public Clazz(){}

    @Deprecated
    public Clazz(Long id,String fullQualifiedName, Long jarId, List<Method> methods) {
        this.id=id;
        this.fullQualifiedName = fullQualifiedName;
        this.jarId = jarId;
        this.methods = methods;
    }

    public Clazz(Long id,String fullQualifiedName, Long jarId, List<Method> methods, String clazzName) {
        this.id=id;
        this.fullQualifiedName = fullQualifiedName;
        this.jarId = jarId;
        this.methods = methods;
        this.clazzName = clazzName;
    }

    public Clazz(Long id,String fullQualifiedName, Long jarId, List<Method> methods, String clazzName, String packageName,Boolean isDeprecated) {
        this.id=id;
        this.fullQualifiedName = fullQualifiedName;
        this.jarId = jarId;
        this.methods = methods;
        this.clazzName = clazzName;
        this.packageName = packageName;
        this.isDeprecated = isDeprecated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullQualifiedName() {
        return fullQualifiedName;
    }

    public void setFullQualifiedName(String fullQualifiedName) {
        this.fullQualifiedName = fullQualifiedName;
    }

    public Long getJarId() {
        return jarId;
    }

    public void setJarId(Long jarId) {
        this.jarId = jarId;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Boolean getDeprecated() {
        return isDeprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        isDeprecated = deprecated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clazz clazz = (Clazz) o;
        return Objects.equals(id, clazz.id) &&
                Objects.equals(fullQualifiedName, clazz.fullQualifiedName) &&
                Objects.equals(jarId, clazz.jarId) &&
                Objects.equals(packageName, clazz.packageName) &&
                Objects.equals(clazzName, clazz.clazzName) &&
                Objects.equals(methods, clazz.methods) &&
                Objects.equals(isDeprecated, clazz.isDeprecated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullQualifiedName, jarId, packageName, clazzName, methods, isDeprecated);
    }

    @Override
    public String toString() {
        return "Clazz{" +
                "id=" + id +
                ", fullQualifiedName='" + fullQualifiedName + '\'' +
                ", jarId=" + jarId +
                ", packageName='" + packageName + '\'' +
                ", clazzName='" + clazzName + '\'' +
                ", methods=" + methods +
                ", isDeprecated=" + isDeprecated +
                '}';
    }
}
