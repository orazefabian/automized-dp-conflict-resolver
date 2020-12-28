package dp.conflict.resolver.parse;

import dp.conflict.resolver.tree.CallNode;

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
                parseJarConnections(node);
            }
        }
    }

    /**
     * recursive function that traverses the call tree bottom-up and computes the facts
     *
     * @param node {@link CallNode}
     */
    private void parsePreviousNodes(CallNode node) {
        if (node != null) {
            // check if root node of tree is reached
            if (node.getPrevious() == null) {
                parseRootJarFact(node.getFromJar());
            } else {
                parseJarFact(node.getFromJar());
            }
            parseHasClassFact(node.getFromJar());
            parseHasMethodFact(node.getFromJar(), node.getClassName().replace(".", File.separator));
            parsePreviousNodes(node.getPrevious());
            if (node.getPrevious() != null) parseJarConnections(node);
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
     * parse root node to a jar fact once its reached
     *
     * @param jarPath the full path to the jar/project
     */
    private void parseRootJarFact(String jarPath) {
        if (!this.idMap.keySet().contains(jarPath)) {
            this.idMap.put(jarPath, this.currJarID++);
            int nextJarID = this.idMap.get(jarPath);
            String[] construct = jarPath.split(File.separator);
            String rootArtifact = construct[construct.length - 1];
            this.factsBuilder.append("\njar(").append(nextJarID).append(",\"rootProject\"").append(",\"").append(rootArtifact).append("\", \"1.0\").\n");
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

            // generate also method facts for curr class
            // parseHasMethodFact(jarPath, cl.toString().replace(".class", ""));
        }
    }

    /**
     * parser function that generates method facts following the signature: hasMethod(JarID, FullQualifiedClass, MethodSignature).
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
            String methodSignature = methodModifiers[methodModifiers.length - 1] + mth.toString().substring(mth.toString().indexOf("("), mth.toString().indexOf(";"));
            // create fact which maps method to a class and jar
            this.factsBuilder.append("method(").append(this.idMap.get(jarPath)).append(",\"")
                    .append(className.replace(File.separator, ".")).append("\",\"").append(methodSignature).append("\").\n");
        }
    }

    /**
     * parser function that computes facts for modeling edges between jars: jarEdge(FromJarID, ToJarID).
     *
     * @param node the current node that should correspond to the ToJarID
     */
    private void parseJarConnections(CallNode node) {
        String currJar = node.getFromJar();
        String prevJar = node.getPrevious().getFromJar();
        int fromID = this.idMap.get(prevJar);
        int toID = this.idMap.get(currJar);
        this.factsBuilder.append("jarEdge(").append(fromID).append(",").append(toID).append(").\n");
    }

    //TODO: create facts for all optional jars, all different version (locally or otherwise, from central maven repo)

    private void parseClashFact(int jarID, String className, String methodName) {

    }


}
