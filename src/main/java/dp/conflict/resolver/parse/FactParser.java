package dp.conflict.resolver.parse;

import dp.conflict.resolver.tree.CallNode;
import dp.conflict.resolver.tree.Invocation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*********************************
 Created by Fabian Oraze on 23.12.20
 *********************************/

public class FactParser {

    private final List<CallNode> conflictNodes;
    private final StringBuilder factsBuilder;
    private final FileWriter writer;
    private final String ROOT_DIR = System.getProperty("user.dir");
    private final File factsFile;
    private final Map<String, Integer> idMap;
    private int currJarID;

    public FactParser(List<CallNode> conflictNodes) throws IOException {
        this.idMap = new HashMap<>();
        this.conflictNodes = conflictNodes;
        this.factsBuilder = new StringBuilder();
        this.factsFile = new File(ROOT_DIR + File.separator + "facts.lp");
        System.out.println(this.factsFile.getAbsolutePath());
        this.factsFile.createNewFile();
        this.writer = new FileWriter(this.factsFile);
        this.currJarID = 1;
        generateFacts();
        writer.write(this.factsBuilder.toString());
        writer.close();
    }

    public String getFacts() {
        return this.factsBuilder.toString();
    }

    /**
     * computes logical facts for given conflict Nodes {@link CallNode} from a {@link dp.conflict.resolver.tree.CallTree}
     */
    private void generateFacts() {
        //TODO: parse conflicts to facts
        for (CallNode node : this.conflictNodes) {
            parseJarFact(node.getFromJar());
            parseHasClassFact(node.getFromJar());
            parseHasMethodFact(node.getFromJar(), node.getClassName().replace(".", File.separator));
            if (node.getPrevious() != null) {
                parsePreviousNodes(node.getPrevious());
            }
        }
    }

    /**
     * recursive function that traverses the call tree bottom-up and computes the facts
     *
     * @param node {@link CallNode}
     */
    private void parsePreviousNodes(CallNode node) {
        if (node.getPrevious() != null) {
            // check if root node of tree is reached
            /*if (node.getPrevious() == null) {
                parseRootJarFact(node.getFromJar());
            }*/
            parseJarFact(node.getFromJar());
            parseHasClassFact(node.getFromJar());
            parseHasMethodFact(node.getFromJar(), node.getClassName().replace(".", File.separator));
            parsePreviousNodes(node.getPrevious());
            parseInvocationFact(node.getInvocations());
        }
    }

    /**
     * parser function that constructs invocation facts for a given list of invocations from a node:
     * invocation(FromJarID, ToJarID, Object, MethodName, paramCount).
     *
     * @param invocations a list of invocation objects given by a CallNode
     */
    private void parseInvocationFact(List<Invocation> invocations) {
        for (Invocation invocation : invocations) {
            int fromID = this.idMap.get(invocation.getParentNode().getFromJar());
            int toID = this.idMap.get(invocation.getNextNode().getFromJar());
            String fromClass = invocation.getDeclaringType();
            String name = invocation.getMethodSignature().substring(0, invocation.getMethodSignature().indexOf("("));
            String signature = invocation.getMethodSignature().split(name)[1];
            int paramCount = computeParamCount(signature);
            this.factsBuilder.append("invocation(").append(fromID).append(",").append(toID).append(",\"").append(fromClass).append("\",\"")
                    .append(name).append("\",").append(paramCount).append(").\n");
        }
    }

    /**
     * parser function that constructs a jar fact with the signature: jar(ID, GroupID, ArtifactID, Version).
     *
     * @param jarPath the full path to the jar
     */
    private void parseJarFact(String jarPath) {
        String repoSeparator = "repository" + File.separator;
        String[] construct = jarPath.split(File.separator);
        String version = construct[construct.length - 2];
        String artifactID = construct[construct.length - 3];
        String groupID = jarPath.substring(jarPath.indexOf(repoSeparator) + repoSeparator.length(), jarPath.indexOf(artifactID) - 1).replace(File.separator, ".");
        if (!this.idMap.keySet().contains(jarPath)) {
            this.idMap.put(jarPath, this.currJarID++);
            int nextJarID = this.idMap.get(jarPath);
            // this line creates the fact in asp language syntax
            this.factsBuilder.append("\njar(").append(nextJarID).append(",\"").append(groupID).append("\",\"").append(artifactID).append("\",\"").append(version).append("\").\n");
        }
    }

    /**
     * parser function that constructs a has class fact in following signature: hasClass(JarID, FullQualifiedClass).
     *
     * @param jarPath the full path to the jar
     */
    private void parseHasClassFact(String jarPath) {
        String content = null;
        try {
            content = JarParser.parseJarClasses(jarPath);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        // filter the content for valid classes
        Object[] objects = Arrays.stream(content.split("\n")).filter(s -> {
            if (s.endsWith(".class")) return true;
            return false;
        }).toArray();
        for (Object cl : objects) {
            // this line creates the fact for the jarClass
            this.factsBuilder.append("class(").append(this.currJarID - 1).append(",\"")
                    .append(cl.toString().replace(".class", "").replace(File.separator, ".")).append("\").\n");
        }
    }

    /**
     * parser function that generates method facts following the signature: hasMethod(JarID, FullQualifiedClass, ParamCount).
     *
     * @param jarPath   the full path to the jar
     * @param className the fully qualified Class name, separated by file separators
     */
    private void parseHasMethodFact(String jarPath, String className) {
        String content = null;
        try {
            content = JarParser.parseJarContent(jarPath, className);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        Object[] methods = Arrays.stream(content.split("\n")).filter(s -> {
            if (s.endsWith(";")) return true;
            return false;
        }).toArray();
        for (Object mth : methods) {
            String[] methodModifiers = mth.toString().substring(0, mth.toString().indexOf("(")).split(" ");
            String methodName = methodModifiers[methodModifiers.length - 1];
            String methodSignature = mth.toString().substring(mth.toString().indexOf("("), mth.toString().indexOf(";"));
            int paramCount = computeParamCount(methodSignature);
            // create fact which maps method to a class and jar
            this.factsBuilder.append("method(").append(this.idMap.get(jarPath)).append(",\"")
                    .append(className.replace(File.separator, ".")).append("\",\"").append(methodName)
                    .append("\",").append(paramCount).append(").\n");
        }
    }

    /**
     * helper function to get the count of parameters of a method
     *
     * @param methodSignature the signature of the method e.g. "(X,Y)"
     * @return the amount of parameters
     */
    private int computeParamCount(String methodSignature) {
        if (methodSignature.equals("()")) return 0;
        String[] params = methodSignature.split(",");
        return params.length;
    }

    /**
     * parser function that computes facts for modeling edges between jars: jarEdge(FromJarID, ToJarID).
     *
     * @param node the current node that should correspond to the ToJarID
     */
    private void parseJarConnections(CallNode node) {
        String currJar = node.getFromJar();
        String prevJar = node.getPrevious().getFromJar();
        int fromID = 0;
        if (this.idMap.keySet().contains(prevJar)) fromID = this.idMap.get(prevJar);
        int toID = this.idMap.get(currJar);
        this.factsBuilder.append("jarEdge(").append(fromID).append(",").append(toID).append(").\n");
    }

    //TODO: create facts for all optional jars, all different version (locally or otherwise, from central maven repo)

    private void parseClashFact(int jarID, String className, String methodName) {

    }


}
