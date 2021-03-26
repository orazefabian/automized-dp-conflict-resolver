package dp.resolver.tree.element;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/*********************************
 Created by Fabian Oraze on 03.12.20
 *********************************/

public class CallNode implements Comparable<CallNode> {

    private String className;
    private String fromJar;
    private List<Invocation> invocations;
    private Set<String> currPomJarDependencies;
    private CallNode previous;

    public CallNode(String className, String fromJar, Set<String> jarDependencies, CallNode previous) {
        this.className = className;
        this.fromJar = fromJar;
        this.invocations = new ArrayList<>();
        this.currPomJarDependencies = jarDependencies;
        this.previous = previous;
    }

    public CallNode getPrevious() {
        return previous;
    }

    public void setPrevious(CallNode previous) {
        this.previous = previous;
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

    public boolean isLeafNode() {
        for (Invocation invocation : this.invocations) {
            if (invocation.getNextNode() != null) return false;
        }
        return true;
    }

    @Override
    public int compareTo(@NotNull CallNode o) {
        String compClassName = o.getClassName();
        return compClassName.compareTo(className);
    }

    @Override
    public boolean equals(Object o) {
        CallNode comp = (CallNode) o;
        return comp.getFromJar().equals(this.fromJar) && comp.getClassName().equals(this.className);
    }
}
