package dp.conflict.resolver.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/*********************************
 Created by Fabian Oraze on 03.12.20
 *********************************/

public class CallNode {

    private String className;
    private String fromJar;
    private List<Invocation> invocations;
    private Set<String> currPomJarDependencies;

    public CallNode(String className, String fromJar, Set<String> jarDependencies) {
        this.className = className;
        this.fromJar = fromJar;
        this.invocations = new ArrayList<>();
        this.currPomJarDependencies = jarDependencies;
    }

    public Set<String> getCurrPomJarDependencies() {
        return currPomJarDependencies;
    }

    public void setCurrPomJarDependencies(Set<String> currPomJarDependencies) {
        this.currPomJarDependencies = currPomJarDependencies;
    }

    public void addInvocation(Invocation call) {
        this.invocations.add(call);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFromJar() {
        return fromJar;
    }

    public void setFromJar(String fromJar) {
        this.fromJar = fromJar;
    }

    public List<Invocation> getInvocations() {
        return invocations;
    }

}
