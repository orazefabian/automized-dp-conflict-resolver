package ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*********************************
 Created by Fabian Oraze on 28.11.20
 *********************************/

public class CallTracer {

    private HashMap<String, Boolean> jars;
    private List<List<String>> invokedMethods;
    private SpoonModel model;

    public CallTracer(String pathToRepo) {
        this.jars = new HashMap<>();
        this.invokedMethods = new ArrayList<>();
        this.model = SpoonModel.getSpoonModel(pathToRepo);
    }

    public void callSpoonASTModes() {
        this.jars.putAll(this.model.computeJarPaths());
        this.invokedMethods.addAll(this.model.iterateMethods());
        while (jarsToTraverseLeft()) {
            String targetNew = getNonTraversedJar();
            if (this.model.setJarLauncher(targetNew)){
                this.invokedMethods.addAll(this.model.iterateMethods());
                //TODO: make sure dependencies are not traversed endlessly in a circular path
                try {
                    this.jars.putAll(this.model.computeJarPaths());
                } catch (NullPointerException e) {
                    System.out.println("can not add null to a hashMap");
                }
            } else {
                this.jars.remove(targetNew);
            }
        }
    }

    private String getNonTraversedJar() {
        for (String path : this.jars.keySet()) {
            if (!this.jars.get(path)) {
                this.jars.put(path, true);
                return path;
            }
        }
        return null;
    }

    private boolean jarsToTraverseLeft() {
        for (Boolean traversed : this.jars.values()) {
            if (!traversed) return true;
        }
        return false;
    }

    public HashMap<String, Boolean> getJars() {
        return jars;
    }

    public List<List<String>> getInvokedMethods() {
        return invokedMethods;
    }

}
