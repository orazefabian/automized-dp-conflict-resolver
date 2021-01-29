package dp.conflict.resolver.parse;

import dp.conflict.resolver.base.ImplSpoon;
import dp.conflict.resolver.loader.CentralMavenAPI;
import dp.conflict.resolver.tree.CallNode;
import dp.conflict.resolver.tree.Invocation;
import org.apache.maven.pom._4_0.Dependency;
import org.apache.maven.pom._4_0.Model;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static dp.conflict.resolver.parse.JarParser.getClassNames;
import static dp.conflict.resolver.parse.JarParser.getContentNames;

/*********************************
 Created by Fabian Oraze on 23.12.20
 *********************************/

public class FactParser {

    private List<CallNode> conflictNodes;
    private StringBuilder factsBuilder;
    private FileWriter writer;
    private final static String ROOT_DIR = System.getProperty("user.dir") + "/src/main/java/dp/conflict/resolver/asp";
    private File factsFile;
    private Map<String, Integer> idMap;
    private Set<String> alreadyLoadedJars;
    private int currJarID;
    private AnswerObject answerObject;

    public FactParser(List<CallNode> conflictNodes) throws IOException {
        // check weather conflicts are empty
        if (conflictNodes.size() == 0) {
            System.out.println("No conflicts detected");
        } else {
            init(conflictNodes);
        }
    }

    /**
     * setup method
     *
     * @param nodeList list of Nodes representing the leaf nodes
     */
    private void init(List<CallNode> nodeList) throws IOException {
        this.idMap = new HashMap<>();
        this.alreadyLoadedJars = new HashSet<>();
        this.conflictNodes = nodeList;
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

    /**
     * retrieve an object representation of a clingo answer after solving program
     *
     * @return {@link AnswerObject}
     * @throws IOException          when reading input files fails
     * @throws InterruptedException when clingo process gets interrupted
     * @throws NoConflictException  if there are no conflicts to solve
     */
    public AnswerObject getAnswerObject() throws IOException, InterruptedException, NoConflictException {
        if (this.answerObject == null) this.answerObject = new AnswerObject();
        this.answerObject.setIDMap(this.idMap);
        this.answerObject.solve();
        return this.answerObject;
    }

    public String getFacts() {
        return this.factsBuilder.toString();
    }

    /**
     * computes logical facts for given conflict Nodes {@link CallNode} from a {@link dp.conflict.resolver.tree.CallTree}
     */
    private void generateFacts() {
        // compute facts for call tree
        for (CallNode node : this.conflictNodes) {
            /*parseJarFact(node);
            generateOptionalJarFacts(node);
            if (node.getPrevious() != null) {
                parsePreviousNodes(node.getPrevious());
            }*/
            parsePreviousNodes(node);
        }
    }


    /**
     * recursive function that traverses the call tree bottom-up and computes the facts
     *
     * @param node {@link CallNode}
     */
    private void parsePreviousNodes(CallNode node) {
        if (node.getPrevious() != null) {
            parseJarFact(node);
            generateOptionalJarFacts(node);
            parsePreviousNodes(node.getPrevious());
            parseInvocationFact(node.getInvocations());
            parseDPConnection(node);
        } else {
            try {
                parseRoot(node);
            } catch (NullPointerException e) {
                System.out.println("Not yet finished with tree traversal");
            }
        }
    }

    /**
     * parses all root invocations as facts: invocation(0["indicates root project"], toJarID, QualifiedName, MethodName, ParameterCount).
     *
     * @param node the root node of the conflict tree
     */
    private void parseRoot(CallNode node) throws NullPointerException {
        for (Invocation inv : node.getInvocations()) {
            int fromID = 0;
            int toID = this.idMap.get(inv.getNextNode().getFromJar());
            String fromClass = inv.getDeclaringType();
            String name = inv.getMethodSignature().substring(0, inv.getMethodSignature().indexOf("("));
            String signature = inv.getMethodSignature().split(name)[1];
            int paramCount = computeParamCount(signature);
            this.factsBuilder.append("\ninvocation(").append(fromID).append(",").append(toID).append(",\"").append(fromClass).append("\",\"")
                    .append(name).append("\",").append(paramCount).append(").\n");
        }
    }

    /**
     * function to load all possible versions of a artifact and then generate facts, jar(...), class(...), method(...)
     *
     * @param node the CallNode which contains the jar where it is from
     */
    private void generateOptionalJarFacts(CallNode node) {
        String jarPath = node.getFromJar();
        if (this.alreadyLoadedJars.add(jarPath)) {
            try {
                String repoSeparator = "repository" + File.separator;
                String[] construct = jarPath.split(File.separator);
                String artifactID = construct[construct.length - 3];
                String groupID = jarPath.substring(jarPath.indexOf(repoSeparator) + repoSeparator.length(), jarPath.indexOf(artifactID) - 1).replace(File.separator, ".");
                try {
                    CentralMavenAPI.getAllVersionsFromCMR(groupID, artifactID);
                } catch (IOException | ParserConfigurationException | SAXException e) {
                    System.err.println("could not download versions fromm central repo");
                }
                // make file at curr jar and generate facts for each version
                String pathToVersionsDir = node.getFromJar().split(construct[construct.length - 2])[0];
                File currJarDir = new File(pathToVersionsDir);
                for (File dir : currJarDir.listFiles()) {
                    if (dir.isDirectory()) {
                        String optionJarVersionPath = pathToVersionsDir + dir.getName() + File.separator + artifactID + "-" + dir.getName() + ".jar";
                        parseOptionalJarFacts(optionJarVersionPath);
                        parseDPConnection(optionJarVersionPath);
                    }
                }
            } catch (StringIndexOutOfBoundsException e) {
                System.err.println("could not parse optional jar fact");
            }
        }
    }

    /**
     * parser function that constructs invocation facts for a given list of invocations from a node:
     * invocation(FromJarID, ToJarID, Object, MethodName, paramCount).
     *
     * @param invocations a list of invocation objects given by a CallNode
     */
    private void parseInvocationFact(List<Invocation> invocations) {
        try {
            for (Invocation invocation : invocations) {
                int fromID = this.idMap.get(invocation.getParentNode().getFromJar());
                int toID = this.idMap.get(invocation.getNextNode().getFromJar());
                String fromClass = invocation.getDeclaringType();
                String name = invocation.getMethodSignature().substring(0, invocation.getMethodSignature().indexOf("("));
                String signature = invocation.getMethodSignature().split(name)[1];
                int paramCount = computeParamCount(signature);
                this.factsBuilder.append("\ninvocation(").append(fromID).append(",").append(toID).append(",\"").append(fromClass).append("\",\"")
                        .append(name).append("\",").append(paramCount).append(").\n");
            }
        } catch (NullPointerException e) {
            System.err.println("Parsing invocation not possible");
        }
    }

    /**
     * parser function that constructs a jar fact with the signature: jar(ID, GroupID, ArtifactID, Version).
     * then calls the other needed parser functions
     *
     * @param node CallNode with the full path to the jar, invocations and className
     */
    private void parseJarFact(CallNode node) {
        String jarPath = node.getFromJar();
        String repoSeparator = "repository" + File.separator;
        String[] construct = jarPath.split(File.separator);
        String version = construct[construct.length - 2];
        String artifactID = construct[construct.length - 3];
        String groupID;
        try {
            // TODO: make jars that cannot be traced completely automatically included!?

            groupID = jarPath.substring(jarPath.indexOf(repoSeparator) + repoSeparator.length(), jarPath.indexOf(artifactID) - 1).replace(File.separator, ".");
            if (!this.idMap.containsKey(jarPath)) {
                this.idMap.put(jarPath, this.currJarID++);
                int nextJarID = this.idMap.get(jarPath);
                // this line creates the fact in asp language syntax
                this.factsBuilder.append("\njar(").append(nextJarID).append(",\"").append(groupID).append("\",\"").append(artifactID).append("\",\"").append(version).append("\").\n");
            }
            // now compute the rest of the needed facts
            // parseClassFact(jarPath); class facts are not needed because method facts contain all information
            parseMethodFact(jarPath, node.getClassName().replace(".", File.separator));
        } catch (StringIndexOutOfBoundsException e) {
            System.err.println("Could not parse jar fact");
        }
    }

    /**
     * parser function that constructs a has class fact in following signature: hasClass(JarID, FullQualifiedClass).
     *
     * @param jarPath the full path to the jar
     */
    private void parseClassFact(String jarPath) {
        Object[] objects = getClassNames(jarPath);
        for (Object cl : objects) {
            // this line creates the fact for the jarClass
            this.factsBuilder.append("class(").append(this.idMap.get(jarPath)).append(",\"")
                    .append(cl.toString().replace(".class", "").replace(File.separator, ".")).append("\").\n");
        }
    }


    /**
     * parser function that generates method facts following the signature: hasMethod(JarID, FullQualifiedClass, ParamCount).
     *
     * @param jarPath   the full path to the jar
     * @param className the fully qualified Class name, separated by file separators
     */
    private void parseMethodFact(String jarPath, String className) {
        Object[] content = JarParser.getContentNames(jarPath, className);
        for (Object mth : content) {
            // skip parsing if not method
            if (mth.toString().contains("(") && mth.toString().contains(")")) {
                String[] methodModifiers = mth.toString().substring(0, mth.toString().indexOf("(")).split(" ");
                String methodName = methodModifiers[methodModifiers.length - 1];
                String methodSignature = mth.toString().substring(mth.toString().indexOf("("), mth.toString().indexOf(")") + 1);
                int paramCount = computeParamCount(methodSignature);
                // create fact which maps method to a class and jar
                this.factsBuilder.append("method(").append(this.idMap.get(jarPath)).append(",\"")
                        .append(className.replace(File.separator, ".")).append("\",\"").append(methodName)
                        .append("\",").append(paramCount).append(").\n");
            }
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
     * parser function that computes facts for modeling edges between jars: connection(FromJarID, ToJarID).
     * should be called after previous nodes have already been processed
     *
     * @param node the current node that should correspond to the FromJarID
     */
    private void parseDPConnection(CallNode node) {
        String currJar = node.getFromJar();
        if (this.idMap.containsKey(currJar)) {
            int fromID = this.idMap.get(currJar);
            for (String dp : node.getCurrPomJarDependencies()) {
                int toID = 0;
                if (this.idMap.containsKey(dp)) {
                    toID = this.idMap.get(dp);
                    this.factsBuilder.append("connection(").append(fromID).append(",").append(toID).append(").\n");
                }
            }
        }
    }

    /**
     * overloaded parser function for computing edges between jars, for parsing optional jar dps
     *
     * @param jarPath the full path with .jar ending to a local jar file
     */
    private void parseDPConnection(String jarPath) {
        if (this.idMap.containsKey(jarPath)) {
            int fromID = this.idMap.get(jarPath);
            ImplSpoon impl = new ImplSpoon(jarPath);
            Model model = impl.getPomModel();
            try {
                for (Dependency dp : model.getDependencies().getDependency()) {
                    String construct = jarPath.substring(0, jarPath.indexOf("repository/")) + "repository/";
                    String jarDp = construct + dp.getGroupId().replace(".", File.separator)
                            + File.separator + dp.getArtifactId() + File.separator +
                            dp.getVersion() + File.separator + dp.getArtifactId() + "-" + dp.getVersion() + ".jar";
                    // parse connection if from id and to id are present in idMap
                    if (this.idMap.containsKey(jarDp)) {
                        int toID = this.idMap.get(jarDp);
                        this.factsBuilder.append("connection(").append(fromID).append(",").append(toID).append(").\n");
                    }
                }
            } catch (NullPointerException e) {
                System.err.println("No dependencies for: " + jarPath);
            }

        }
    }

    /**
     * parser function that computes facts for optional jars
     *
     * @param jarPath the full path to the jar ending with .jar
     */
    private void parseOptionalJarFacts(String jarPath) {
        if (!this.idMap.containsKey(jarPath)) {
            this.factsBuilder.append("\n");
            String repoSeparator = "repository" + File.separator;
            String[] construct = jarPath.split(File.separator);
            String version = construct[construct.length - 2];
            String artifactID = construct[construct.length - 3];
            String groupID = jarPath.substring(jarPath.indexOf(repoSeparator) + repoSeparator.length(), jarPath.indexOf(artifactID) - 1).replace(File.separator, ".");
            this.idMap.put(jarPath, this.currJarID++);
            int nextJarID = this.idMap.get(jarPath);
            // this line creates the fact in asp language syntax
            this.factsBuilder.append("\njar(").append(nextJarID).append(",\"").append(groupID).append("\",\"").append(artifactID).append("\",\"").append(version).append("\").\n");
            Object[] classNames = getClassNames(jarPath);
            for (Object cl : classNames) {
                // this line creates the fact for the jarClass
                String clName = cl.toString().replace(".class", "").replace(File.separator, ".");
                // this.factsBuilder.append("class(").append(this.idMap.get(jarPath)).append(",\"").append(clName).append("\").\n");
                Object[] methodNames = getContentNames(jarPath, cl.toString().replace(".class", ""));
                for (Object mt : methodNames) {
                    if (mt.toString().contains("(") && mt.toString().contains(")")) {
                        String[] methodModifiers = mt.toString().substring(0, mt.toString().indexOf("(")).split(" ");
                        String methodName = methodModifiers[methodModifiers.length - 1];
                        String methodSignature = mt.toString().substring(mt.toString().indexOf("("), mt.toString().indexOf(";"));
                        int paramCount = computeParamCount(methodSignature);
                        // create fact which maps method to a class and jar
                        this.factsBuilder.append("method(").append(this.idMap.get(jarPath)).append(",\"")
                                .append((clName).replace(File.separator, ".")).append("\",\"").append(methodName)
                                .append("\",").append(paramCount).append(").\n");
                    }
                }
            }
        }
    }


}
