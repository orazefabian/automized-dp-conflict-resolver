package dp.resolver.parse.entity;

/*********************************
 Created by Fabian Oraze on 03.02.21
 *********************************/

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@SequenceGenerator(
        name = "jarIdSequence",
        initialValue = 1,
        allocationSize = 1,
        sequenceName = "jarIdSequence"
)public class Jar implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jarIdSequence")
    private Long id;

    @OneToMany(mappedBy="jarId")
    private List<Clazz> clazzes=new ArrayList<Clazz>();

    @Column(columnDefinition = "TEXT")
    private String groupId;
    @Column(columnDefinition = "TEXT")
    private String artifactId;
    @Column(columnDefinition = "TEXT")
    private String version;

    @Column(columnDefinition = "TEXT")
    private String fileName;
    @Column(columnDefinition = "TEXT")
    private String downloadUrl;

    private Date publishDate;

    private Boolean processedClazzExtraction=false;

    private Long usageCount;
    private Long clazzCount;
    private boolean isIncludedInFixing;

    public Jar(){}

    public Jar(Long id,List<Clazz> clazzes, String groupId, String artifactId, String version, String fileName, String downloadUrl, Date publishDate, Boolean processedClazzExtraction) {
        this.id=id;
        this.clazzes = clazzes;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.fileName = fileName;
        this.downloadUrl = downloadUrl;
        this.publishDate = publishDate;
        this.processedClazzExtraction = processedClazzExtraction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Clazz> getClazzes() {
        return clazzes;
    }

    public void setClazzes(List<Clazz> clazzes) {
        this.clazzes = clazzes;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Boolean getProcessedClazzExtraction() {
        return processedClazzExtraction;
    }

    public void setProcessedClazzExtraction(Boolean processedClazzExtraction) {
        this.processedClazzExtraction = processedClazzExtraction;
    }

    public Long getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Long usageCount) {
        this.usageCount = usageCount;
    }

    public Long getClazzCount() {
        return clazzCount;
    }

    public void setClazzCount(Long clazzCount) {
        this.clazzCount = clazzCount;
    }

    public boolean isIncludedInFixing() {
        return isIncludedInFixing;
    }

    public void setIncludedInFixing(boolean includedInFixing) {
        isIncludedInFixing = includedInFixing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jar jar = (Jar) o;

        if (id != null ? !id.equals(jar.id) : jar.id != null) return false;
        if (clazzes != null ? !clazzes.equals(jar.clazzes) : jar.clazzes != null) return false;
        if (groupId != null ? !groupId.equals(jar.groupId) : jar.groupId != null) return false;
        if (artifactId != null ? !artifactId.equals(jar.artifactId) : jar.artifactId != null) return false;
        if (version != null ? !version.equals(jar.version) : jar.version != null) return false;
        if (fileName != null ? !fileName.equals(jar.fileName) : jar.fileName != null) return false;
        if (downloadUrl != null ? !downloadUrl.equals(jar.downloadUrl) : jar.downloadUrl != null) return false;
        if (publishDate != null ? !publishDate.equals(jar.publishDate) : jar.publishDate != null) return false;
        return processedClazzExtraction != null ? processedClazzExtraction.equals(jar.processedClazzExtraction) : jar.processedClazzExtraction == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (clazzes != null ? clazzes.hashCode() : 0);
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + (artifactId != null ? artifactId.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (downloadUrl != null ? downloadUrl.hashCode() : 0);
        result = 31 * result + (publishDate != null ? publishDate.hashCode() : 0);
        result = 31 * result + (processedClazzExtraction != null ? processedClazzExtraction.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Jar{" +
                "id=" + id +
//                ", clazzes=" + clazzes +
                ", groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                ", fileName='" + fileName + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", publishDate=" + publishDate +
                ", processedClazzExtraction=" + processedClazzExtraction +
                ", usageCount=" + usageCount +
                ", clazzCount=" + clazzCount +
                ", isIncludedInFixing=" + isIncludedInFixing +
                '}';
    }


}