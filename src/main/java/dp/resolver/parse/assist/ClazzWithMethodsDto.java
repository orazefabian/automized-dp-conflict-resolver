package dp.resolver.parse.assist;

import java.util.List;

/*********************************
 Created by Fabian Oraze on 03.02.21
 *********************************/
public class ClazzWithMethodsDto {
    private String clazzName;
    private List<MethodInformation> methods;
    private boolean deprecated;

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public List<MethodInformation> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodInformation> methods) {
        this.methods = methods;
    }
}
