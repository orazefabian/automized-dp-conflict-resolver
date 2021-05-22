package dp.resolver.parse.entity;


import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MessagingJar implements Serializable {
    private List<MessagingClazz> clazzes = new ArrayList<MessagingClazz>();

    private String groupId;
    private String artifactId;
    private String version;

    private String fileName;
    private String downloadUrl;

    public void fillJarFromFullPath(String pathToJar) {
        String[] components = pathToJar.substring(pathToJar.indexOf("repository")).split(Pattern.quote(File.separator));
        int count = components.length - 1;
        setClazzes(new ArrayList<>());
        setFileName(components[count--]);
        setVersion(components[count--]);
        setArtifactId(components[count--]);

        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= count; i++) {
            builder.append(components[i]);
            if (i != count) builder.append(".");
        }
        setGroupId(builder.toString());
    }

    public List<MessagingClazz> getClazzes() {
        return clazzes;
    }

    public void setClazzes(List<MessagingClazz> clazzes) {
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
}
