package dp.resolver.tree;

import dp.api.maven.CentralMavenAPI;
import dp.resolver.parse.assist.AssistParser;
import dp.resolver.parse.entity.MessagingClazz;
import dp.resolver.tree.element.CallNode;
import dp.resolver.tree.element.Invocation;
import dp.resolver.tree.model.*;
import spoon.compiler.ModelBuildingException;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.*;

import static java.lang.Character.isDigit;

/*********************************
 Created by Fabian Oraze on 03.12.20
 *********************************/

public class CallTree implements Tree {

    private final List<CallNode> startNodes;
    private final String targetProjectPath;
    private final ModelFactory modelFactory;
    private final Map<String, Boolean> jars;
    private Set<Invocation> currLeaves;
    private final List<CallNode> conflicts;
    private final Set<String> neededJars;
    private final AnswerObject answerObject;
    private CallModel model;

    /**
     * Tree data structure which contains all method call traces from a given root project
     *
     * @param targetProjectPath path to maven project which is to be analyzed, MUST end with a "/" (File separator)
     */
    public CallTree(String targetProjectPath, AnswerObject answerObject) {
        if (targetProjectPath.endsWith(File.separator)) {
            this.targetProjectPath = targetProjectPath;
        } else {
            this.targetProjectPath = targetProjectPath + File.separator;
        }
        this.answerObject = answerObject;
        this.modelFactory = new ModelFactoryImpl();
        this.startNodes = new ArrayList<>();
        this.jars = new HashMap<>();
        this.neededJars = new HashSet<>();
        this.conflicts = new ArrayList<>();
        this.currLeaves = new HashSet<>();
        initModel();
        //setInitialLeaves();
    }

    private void setInitialLeaves() {
        // set current leaf elements
        this.currLeaves = new HashSet<>();
        for (CallNode node : this.startNodes) {
            currLeaves.addAll(node.getInvocations());
        }
    }

    @Override
    public List<CallNode> getCallTree() {
        return this.startNodes;
    }

    @Override
    public void computeCallTree() {
        try {
            createNewModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.model.analyzeModel();
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
        switch (type) {
            case TYPE_1:
                for (CallNode call : trace) {
                    for (CallNode checkCall : trace) {
                        if (checkForConflictType1(call, checkCall)) {
                            this.conflicts.add(call);
                        }
                    }
                }
                break;
            case TYPE_2:
                for (CallNode call : trace) {
                    for (CallNode checkCall : trace) {
                        if (checkForConflictType2(call, checkCall)) {
                            this.conflicts.add(call);
                        }
                    }
                }
                break;
            case TYPE_3:
                for (CallNode call : trace) {
                    if (checkForConflictType3(call)) {
                        this.conflicts.add(call);
                    }
                }
        }
    }


    /**
     * helper function which checks if a callNode is a leaf node, this type sees all nodes as potential conflicts
     *
     * @param call {@link CallNode}
     * @return true if node is a leaf object of current tree
     */
    private boolean checkForConflictType3(CallNode call) {
        if (call.getPrevious() == null) return false; // do not add root nodes
        else {
            for (Invocation inv : call.getInvocations()) {
                if (inv.getNextNode() != null) return false;
            }
        }
        return true;
    }

    /**
     * helper function which checks if two different CallNodes cause a possible conflict (must have the same fullyQualifiedName and be from different Jars)
     *
     * @param first  {@link CallNode}
     * @param second {@link CallNode}
     * @return true if two calleNodes cause a possible thread
     */
    private boolean checkForConflictType2(CallNode first, CallNode second) {
        return !first.equals(second) && first.getClassName().equals(second.getClassName()) && !first.getFromJar().equals(second.getFromJar());
    }

    /**
     * helper function which checks if two different CallNodes cause a definitive conflict (Objects that use methods in different jars with different method signatures)
     *
     * @param first  {@link CallNode}
     * @param second {@link CallNode}
     * @return true if they definitely cause an error
     */
    private boolean checkForConflictType1(CallNode first, CallNode second) {
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

    @Override
    public List<CallNode> getConflicts(ConflictType type) {
        computeConflicts(type);
        return this.conflicts;
    }

    @Override
    public Set getNeededJars() {
        return this.neededJars;
    }

    /**
     * function that recursively fills a set with all nodes from the given root node
     *
     * @param callNode the root node
     * @param trace    the set with call nodes that is being filled
     */
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
            this.model = modelFactory.createCallModelFromMaven(targetProjectPath, this.currLeaves);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.jars.putAll(this.model.getDependenciesToJarPaths());
        } catch (IOException | InterruptedException | JAXBException e) {
            e.printStackTrace();
        }
        this.model.analyzeModel();
        this.startNodes.addAll(this.model.getCallNodes());
    }

    /**
     * helper function to create new {@link CallModel} for next jar, after analyzing previous one
     */
    private void createNewModel() throws NullPointerException {
        removeNonUsedOrNeededJars();
        String nextJar = getNonTraversedJar();
        // save already traversed jars for later conflict search
        try {
            this.model = modelFactory.createCallModelFromJar(nextJar, this.currLeaves);
            //this.model.setCallNodes(prevCallNodes);
            this.jars.putAll(this.model.getDependenciesToJarPaths());
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

    private void removeNonUsedOrNeededJars() {
        List<String> jarsToRemove = new ArrayList<>();
        for (String jarPath : this.jars.keySet()) {
            // remove non used jars
            checkIfJarExists(jarPath);
            if (checkIfJarUsedOrNeeded(jarPath)) jarsToRemove.add(jarPath);
        }
        for (String key : jarsToRemove) {
            this.jars.remove(key);
            if (this.model.getCurrProjectPath().equals(this.targetProjectPath)) {
                if (this.model instanceof MavenSpoonModel && checkIfJarIsPossiblyNeeded(key)) {
                    this.neededJars.add(key); // if jar is possibly needed it will get added to needed jars for safety reasons
                } else {
                    this.answerObject.addBloatedJar(key);
                    // add jars that are directly bloated (root pom)
                }
            }
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
            CentralMavenAPI.downloadMissingFiles(nextJar);
        }
    }

    /**
     * helper function to compute the current leaf elements of the whole call tree
     * new leaf elements are appended via the next() method from callNodes class to invocation objects
     * old leaves are then removed
     */
    private void computeLeafElements() {
        List<Invocation> toBeRemoved = new ArrayList<>();
        List<Invocation> toBeAdded = new ArrayList<>();
        for (Invocation invocation : this.currLeaves) {
            if (!invocation.isLeafInvocation()) {
                toBeAdded.addAll(invocation.getNextNode().getInvocations());
                toBeRemoved.add(invocation);
            }
        }
        this.currLeaves.addAll(toBeAdded);
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
     * function that checks if a given jar is used by any call of the current invocations or if its needed
     *
     * @param jarPath String representation of the complete path to the Jar to be checked for usage
     * @return true if the given jar is not used or its per default needed due to annotations
     */
    private boolean checkIfJarUsedOrNeeded(String jarPath) throws NullPointerException {
        if (JDKClassHelper.isPartOfJDKFromFullPath(jarPath)) return true;
        List<MessagingClazz> jarClassList = AssistParser.getJarClassList(jarPath);
        boolean remove = true;
        for (Invocation invocation : this.currLeaves) {
            try {
                for (MessagingClazz clazz : jarClassList) {
                    if (checkIfInvocationDeclaringTypeIsEqualToClass(invocation, clazz)) {
                        remove = false;
                        break;
                    }
                    // directly add jar to neededJars list if on its annotations was used by the model
                    if (checkIfAnnotationIsUsedByRoot(clazz.getFullQualifiedName())) {
                        addJarToNeededListIfNoOtherVersionConflict(jarPath);
                        remove = false;
                        break;
                    }
                }
            } catch (Exception e) {
                // skip to next inovcation
            }
            if (!remove) break;
        }

        return remove;
    }

    private void addJarToNeededListIfNoOtherVersionConflict(String jarPath) {
        String[] construct = jarPath.split("/");
        StringBuilder builder = new StringBuilder();
        boolean mustAdd = true;
        for (int i = 1; i < construct.length; i++) {
            if (isDigit(construct[i].charAt(0))) {
                for (String neededJar : this.neededJars) {
                    if (neededJar.startsWith(builder.toString())) mustAdd = false;
                }
                break;
            } else {
                builder.append("/").append(construct[i]);
            }
        }
        if (mustAdd) this.neededJars.add(jarPath);
    }

    private boolean checkIfInvocationDeclaringTypeIsEqualToClass(Invocation invocation, MessagingClazz clazz) {
        return clazz.getFullQualifiedName().equals(invocation.getDeclaringType());
    }

    private boolean checkIfAnnotationIsUsedByRoot(String clazzName) {
        if (this.model instanceof MavenSpoonModel) {
            return this.model.getAllAnnotations().contains(clazzName);
        } else {
            return false;
        }
    }

    /**
     * checks whether a pom file contains the string prefix of a groupID (does not have to bee a dependency) hence jar is possibly needed
     *
     * @param jarPath path from the jar to be checked
     * @return true if pom file contains the prefix
     */
    private boolean checkIfJarIsPossiblyNeeded(String jarPath) {
        for (Invocation invocation : this.currLeaves) {
            try {
                String groupID = invocation.getDeclaringType()
                        .substring(0, invocation.getDeclaringType().indexOf(".", invocation.getDeclaringType().indexOf(".") + 1));
                String pom = jarPath.replace(".jar", ".pom");
                File pomFile = new File(pom);
                Scanner scanner = new Scanner(pomFile);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.contains(groupID)) {
                        scanner.close();
                        return true;
                    }
                }
                scanner.close();
            } catch (Exception e) {
                // skip invocation@
            }
        }
        return false;
    }


}
