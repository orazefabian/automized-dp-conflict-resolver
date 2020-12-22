package dp.conflict.resolver.tree;

import spoon.compiler.ModelBuildingException;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/*********************************
 Created by Fabian Oraze on 03.12.20
 *********************************/

public class CallTree {

    private final List<CallNode> startNodes;
    private final String targetProjectPath;
    private SpoonModel model;
    private final Map<String, Boolean> jars;
    private final List<Invocation> currLeaves;
    private final List<CallNode> conflicts;
    private final List<String> allUsedJars;

    /**
     * Tree data structure which contains all method call traces from a given root project
     *
     * @param targetProjectPath path to maven project which is to be analyzed
     */
    public CallTree(String targetProjectPath) {
        this.targetProjectPath = targetProjectPath;
        this.startNodes = new ArrayList<>();
        this.jars = new HashMap<>();
        this.allUsedJars = new ArrayList<>();
        this.conflicts = new ArrayList<>();
        initModel();
        this.currLeaves = new ArrayList<>();
        // set current leaf elements
        for (CallNode node : this.startNodes) {
            currLeaves.addAll(node.getInvocations());
        }
    }

    /**
     * @return complete call tree
     */
    public List<CallNode> getCallTree() {
        return this.startNodes;
    }

    /**
     * method which computes the complete call trace for all method invocations from
     * the root project pointing to other dependencies recursively
     */
    public void computeCallTree() {
        createNewModel();
        this.model.iterateClasses(this.currLeaves);
        computeLeafElements();
        if (jarsToTraverseLeft())
            computeCallTree();
    }

    /**
     * helper function which computes the possible conflicts based on a
     * already computed call tree
     *
     * @param type {@link ConflictType} specifies which type of conflict should be computed
     */
    private void computeConflicts(ConflictType type) {
        Set<CallNode> trace = new HashSet<>();
        for (CallNode node : this.startNodes) {
            recursiveSearch(node, trace);
        }
        for (CallNode call : trace) {
            for (CallNode checkCall : trace) {
                switch (type) {
                    case TYPE_1:
                        if (checkForConflictType1(call, checkCall)) {
                            this.conflicts.add(call);
                        }
                        break;
                    case TYPE_2:
                        if (checkForConflictType2(call, checkCall)) {
                            this.conflicts.add(call);
                        }
                        break;
                }
            }
        }
    }

    /**
     * helper function which checks if two different CallNodes cause a possible conflict (must have the same fullyQualifiedName and be from different Jars)
     *
     * @param first  {@link CallNode}
     * @param second {@link CallNode}
     * @return true if two calleNodes cause a possible thread
     */
    private boolean checkForConflictType1(CallNode first, CallNode second) {
        return !first.equals(second) && first.getClassName().equals(second.getClassName()) && !first.getFromJar().equals(second.getFromJar());
    }

    /**
     * helper function which checks if two different CallNodes cause a definitive conflict (Objects that use methods in different jars with different method signatures)
     *
     * @param first  {@link CallNode}
     * @param second {@link CallNode}
     * @return true if they definitely cause an error
     */
    private boolean checkForConflictType2(CallNode first, CallNode second) {
        boolean isOfTypeOne = !first.equals(second) && first.getClassName().equals(second.getClassName()) && !first.getFromJar().equals(second.getFromJar());
        if (!isOfTypeOne) return false;
        for (Invocation invFirst : first.getPrevious().getInvocations()) {
            for (Invocation invSec : second.getPrevious().getInvocations()) {
                // get method names without parameters
                String method1 = invFirst.getMethodSignature().split("\\(")[0];
                String method2 = invSec.getMethodSignature().split("\\(")[0];
                // check if method signatures differ
                if (method1.equals(method2)
                        && invFirst.getDeclaringType().equals(invSec.getDeclaringType())
                        && !invFirst.getMethodSignature().equals(invSec.getMethodSignature())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * computes the conflicts if called the first time
     *
     * @param type {@link ConflictType} enumeration of the type of conflict that should be determined
     * @return {@link List<CallNode>} which cause an issue
     */
    public List<CallNode> getConflicts(ConflictType type) {
        computeConflicts(type);
        return this.conflicts;
    }

    private void recursiveSearch(CallNode callNode, Set<CallNode> trace) {
        trace.add(callNode);
        try {
            for (Invocation inv : callNode.getInvocations()) {
                if (inv.getNextNode() != null) recursiveSearch(inv.getNextNode(), trace);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * initialize first spoon model from a maven launcher for a targetProject
     */
    private void initModel() {
        // compute starting nodes for call tree
        try {
            this.model = new SpoonModel(targetProjectPath, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.jars.putAll(this.model.computeJarPaths());
        } catch (IOException | InterruptedException | JAXBException e) {
            e.printStackTrace();
        }
        this.startNodes.addAll(this.model.iterateClasses(null));
    }

    /**
     * helper function to create new {@link SpoonModel} for next jar
     */
    private void createNewModel() {
        List<CallNode> prevCallNodes = this.model.getCallNodes();
        List<String> jarsToRemove = new ArrayList<>();
        for (String jarPath : this.jars.keySet()) {
            // remove non used jars
            checkIfJarExists(jarPath);
            if (checkIfJarUsed(jarPath)) jarsToRemove.add(jarPath);
        }
        for (String key : jarsToRemove) {
            this.jars.remove(key);
        }
        String nextJar = getNonTraversedJar();
        // save already traversed jars for later conflict search
        this.allUsedJars.add(nextJar);
        try {
            this.model = new SpoonModel(nextJar, true);
            this.model.setCallNodes(prevCallNodes);
            this.jars.putAll(this.model.computeJarPaths());
        } catch (ModelBuildingException e) {
            System.err.println("Error building models: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("No Dependencies found for given project");
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("New launcher model could not be built for: " + nextJar);
            e.printStackTrace();
        }

    }

    /**
     * additional check if jar is not existent, it should be downloaded with the spoonModel
     *
     * @param nextJar jar to be checked if existent
     */
    private void checkIfJarExists(String nextJar) {
        File jar = new File(nextJar);
        File pom = new File(nextJar.replace(".jar", ".pom"));
        if (!jar.exists() || !pom.exists()) {
            System.out.println("Jar and/or pom not found... proceeding with download");
            this.model.downloadMissingFiles(nextJar);
        }
    }

    /**
     * helper function to compute the current leaf elements of the whole call tree
     * new leaf elements are appended via the next() method from callNodes class to invocation objects
     * old leaves are then removed
     */
    private void computeLeafElements() {
        List<Invocation> toBeRemoved = new ArrayList<>();
        for (int i = 0; i < this.currLeaves.size(); i++) {
            Invocation invocation = this.currLeaves.get(i);
            if (invocation.getNextNode() != null) {
                this.currLeaves.addAll(invocation.getNextNode().getInvocations());
                toBeRemoved.add(invocation);
            }
        }
        this.currLeaves.removeAll(toBeRemoved);
    }

    /**
     * helper function to determine if jars which still need to be traversed are left
     *
     * @return true if there are jars left otherwise false
     */
    private boolean jarsToTraverseLeft() {
        for (Boolean traversed : this.jars.values()) {
            if (!traversed) {
                return true;
            }
        }
        return false;
    }

    /**
     * helper function to get the next jar to be analyzed
     *
     * @return String representation of a jar
     */
    private String getNonTraversedJar() {
        for (String path : this.jars.keySet()) {
            if (!this.jars.get(path)) {
                this.jars.put(path, true);
                return path;
            }
        }
        return null;
    }

    /**
     * function that checks if a given jar is used by any call of the current invocations
     *
     * @param jarPath String representation of the complete path to the Jar to be checked for usage
     * @return true if the given jar is not used
     */
    private boolean checkIfJarUsed(String jarPath) {
        String jarContent = null;
        try {
            jarContent = parseJar(jarPath);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        boolean remove = true;
        //for (CallNode node : prevCallNodes) {
        for (Invocation invocation : this.currLeaves) {
            if (jarContent.contains(invocation.getDeclaringType().replace(".", "/"))) {
                remove = false;
                break;
            }
        }
        //}
        return remove;
    }

    /**
     * helper function which parses the contents from a .jar file to a string
     *
     * @param jarPath the complete path to the jar file
     * @return a String containing all declared files (classes) in a jar
     * @throws IOException          if reading file is not possible
     * @throws InterruptedException if the process gets interrupted
     */
    private String parseJar(String jarPath) throws IOException, InterruptedException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream buildOutputStream = new PrintStream(outputStream);

        // maybe adapt for windows?
        String[] structure = jarPath.split("/");
        StringBuilder folder = new StringBuilder();
        String jar = structure[structure.length - 1];
        for (int i = 0; i < structure.length - 1; i++) {
            folder.append(structure[i]).append("/");
        }
        ProcessBuilder pb;

        if (System.getProperty("os.name").startsWith("Windows")) {
            pb = new ProcessBuilder("cmd.exe", "/c", "cd " + folder.toString() + " && jar tf " + jar);
        } else {
            pb = new ProcessBuilder("/bin/bash", "-c", "cd " + folder.toString() + " ; jar tf " + jar);
        }

        Process p = pb.start();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream()));

        //String content = "";
        List<String> lines = new ArrayList<>();
        String line = "";
        System.out.println("Preprocessing jar: " + jarPath + "...");
        while ((line = reader.readLine()) != null) {
            lines.add(line);
            //content = content + line + System.getProperty("line.separator");
            if (buildOutputStream != null) {
                buildOutputStream.println(line);
                //listener //Refactor that only listeners get called here (and make a listener for the print stream
                String finalLine = line;
                // this.repairListeners.forEach(x->x.newBuildLine(finalLine));
            }
        }
        p.waitFor();
        String content = outputStream.toString(StandardCharsets.UTF_8);
        outputStream.flush();
        buildOutputStream.flush();
        return content;
    }


}
