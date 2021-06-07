package dp.resolver.parse;

import dp.resolver.base.ImplSpoon;
import dp.api.maven.CentralMavenAPI;
import dp.resolver.parse.assist.AssistParser;
import dp.resolver.parse.entity.MessagingClazz;
import dp.resolver.parse.entity.MessagingMethod;
import dp.resolver.tree.CallTree;
import dp.resolver.tree.element.CallNode;
import dp.resolver.tree.element.Invocation;
import org.apache.maven.pom._4_0.Dependency;
import org.apache.maven.pom._4_0.Model;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;


/*********************************
 Created by Fabian Oraze on 23.12.20
 *********************************/

public class FactBuilder {

    private List<CallNode> conflictNodes;
    private StringBuilder factsBuilder;
    private FileWriter writer;
    private final static String ROOT_DIR = System.getProperty("user.dir");
    private File factsFile;
    private Map<String, Integer> idMap;
    private Set<String> alreadyLoadedJars;
    private Set<String> alreadyParsedJars;
    private int currJarID;
    private Set<String> neededJars;

    public FactBuilder(List<CallNode> conflictNodes, Set neededJars) throws IOException {
        // check weather conflicts are empty
        if (conflictNodes.size() == 0) {
            System.out.println("No conflicts detected");
        } else {
            init(conflictNodes, neededJars);
        }
    }

    /**
     * setup method
     *
     * @param nodeList   list of Nodes representing the leaf nodes
     * @param neededJars the jars that have to be included
     */
    private void init(List<CallNode> nodeList, Set neededJars) throws IOException {
        this.idMap = new HashMap<>();
        this.neededJars = neededJars;
        this.alreadyLoadedJars = new HashSet<>();
        this.alreadyParsedJars = new HashSet<>();
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

    public Map<String, Integer> getIdMap() {
        return idMap;
    }

    public String getFacts() {
        return this.factsBuilder.toString();
    }

    /**
     * computes logical facts for given conflict Nodes {@link CallNode} from a {@link CallTree}
     */
    private void generateFacts() {
        // compute facts for call tree
        for (CallNode node : this.conflictNodes) {
            parsePreviousNodes(node);
        }
        for (String jar : this.neededJars) {
            parseIncludeJar(jar);
        }

    }


    /**
     * creates a fact to include a jar e.g 'includeJar(jar)'
     *
     * @param jar to be included
     */
    private void parseIncludeJar(String jar) {
        parseJarFact(jar); // parse every needed jar and its method
        Integer id = this.idMap.get(jar);
        this.factsBuilder.append("\nincludeJar(").append(id).append(").\n");
    }


    /**
     * recursive function that traverses the call tree bottom-up and computes the facts
     *
     * @param node {@link CallNode}
     */
    private void parsePreviousNodes(CallNode node) {
        if (node.getPrevious() != null) {
            parseJarFact(node.getFromJar());
            generateOptionalJarFacts(node);
            parsePreviousNodes(node.getPrevious());
            parseDPConnection(node);
        } else {
            try {
                parseRootInvocations(node);
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
    private void parseRootInvocations(CallNode node) throws NullPointerException {
        for (Invocation inv : node.getInvocations()) {
            int fromID = 0;
            String fromClass = inv.getDeclaringType();
            String name = getMethodName(inv);
            String signature = inv.getMethodSignature().split(name)[1];
            int paramCount = computeParamCount(signature);
            if (!fromClass.endsWith(name)) {
                this.factsBuilder.append("\ninvocation(").append(fromID).append(",\"").append(fromClass).append("\",\"")
                        .append(name).append("\",").append(paramCount).append(").\n");
            }
        }
    }

    @NotNull
    private String getMethodName(Invocation inv) {
        String name = inv.getMethodSignature().substring(0, inv.getMethodSignature().indexOf("("));
        if (name.contains("$")) {
            name = name.replace("$", "\\$");
        }
        return name;
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
                String[] construct = jarPath.split(Pattern.quote(File.separator));
                String artifactID = construct[construct.length - 3];
                String groupID = jarPath.substring(jarPath.indexOf(repoSeparator) + repoSeparator.length(), jarPath.indexOf(artifactID) - 1).replace(File.separator, ".");
                try {
                    CentralMavenAPI.getAllVersionsFromCMR(groupID, artifactID, jarPath);
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
     * invocation(FromJarID, Object, MethodName, paramCount).
     *
     * @param invocations a list of invocation objects given by a CallNode
     */
    private void parseInvocationFact(List<Invocation> invocations) {
        try {
            for (Invocation invocation : invocations) {
                // if it is null the next jar in call trace could not be determined and therefore no fact should be generated
                if (isInvocationNeededForFact(invocation)) {
                    int fromID = this.idMap.get(invocation.getParentNode().getFromJar());
                    String declaringClass = invocation.getDeclaringType();
                    String name = getMethodName(invocation);
                    String signature = invocation.getMethodSignature().split(name)[1];
                    int paramCount = computeParamCount(signature);
                    if (!declaringClass.endsWith(name)) {
                        this.factsBuilder.append("\ninvocation(").append(fromID).append(",\"").append(declaringClass).append("\",\"")
                                .append(name).append("\",").append(paramCount).append(").\n");
                    }
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Parsing invocation not possible");
        }
    }

    private boolean isInvocationNeededForFact(Invocation invocation) {
        if (invocation.getNextNode() != null) {
            String fromJar = invocation.getParentNode().getFromJar();
            String toJar = invocation.getNextNode().getFromJar();
            return !fromJar.equals(toJar);
        }
        return false;
    }

    /**
     * parser function that constructs a jar fact with the signature: jar(ID, GroupID, ArtifactID, Version).
     * then calls the other needed parser functions
     *
     * @param jarPath String with the full path to the jar
     */
    private void parseJarFact(String jarPath) {
        String repoSeparator = "repository" + File.separator;
        String[] construct = jarPath.split(Pattern.quote(File.separator));
        String version = construct[construct.length - 2];
        String artifactID = construct[construct.length - 3];
        String groupID;
        try {

            groupID = jarPath.substring(jarPath.indexOf(repoSeparator) + repoSeparator.length(), jarPath.indexOf(artifactID) - 1).replace(File.separator, ".");
            if (!this.idMap.containsKey(jarPath)) {
                this.idMap.put(jarPath, this.currJarID++);
                int nextJarID = this.idMap.get(jarPath);
                // this line creates the fact in asp language syntax
                this.factsBuilder.append("\njar(").append(nextJarID).append(",\"").append(groupID).append("\",\"").append(artifactID).append("\",\"").append(version).append("\").\n");
            }
            parseMethodFact(jarPath);
        } catch (StringIndexOutOfBoundsException e) {
            System.err.println("Could not parse jar fact");
        }
    }

    /**
     * parser function that constructs a has class fact in following signature: class(JarID, FullQualifiedClass).
     *
     * @param jarPath the full path to the jar
     */
    private void parseClassFact(String jarPath) {
        //Object[] objects = JarParser.getClassNames(jarPath);
        List<MessagingClazz> jarClassList = AssistParser.getJarClassList(jarPath);
        for (MessagingClazz clazz : jarClassList) {
            this.factsBuilder.append("class(").append(this.idMap.get(jarPath)).append(",\"")
                    .append(clazz.getClazzName().replace(".class", "")).append("\").\n");
        }
    }


    /**
     * parser function that generates method facts following the signature: hasMethod(JarID, FullQualifiedClass, ParamCount).
     *
     * @param jarPath the full path to the jar
     */
    private void parseMethodFact(String jarPath) {
        try {
            if (this.alreadyParsedJars.add(jarPath)) { //already parsed jars are skipped
                List<MessagingClazz> jarClassList = AssistParser.getJarClassList(jarPath);
                for (MessagingClazz clazz : jarClassList) {
                    for (MessagingMethod methodInformation : clazz.getMethods()) {
                        String methodName = methodInformation.getMethodName();
                        Long numberOfParams = methodInformation.getNumberOfParams();
                        this.factsBuilder.append("method(").append(this.idMap.get(jarPath)).append(",\"").append(clazz.getFullQualifiedName().replace(".class", "")
                                .replace(File.separator, ".")).append("\",\"").
                                append(methodName).append("\",").append(numberOfParams).append(").\n");
                    }
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Jar contains no classes");
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
            String[] construct = jarPath.split(Pattern.quote(File.separator));
            String version = construct[construct.length - 2];
            String artifactID = construct[construct.length - 3];
            String groupID = jarPath.substring(jarPath.indexOf(repoSeparator) + repoSeparator.length(), jarPath.indexOf(artifactID) - 1).replace(File.separator, ".");
            this.idMap.put(jarPath, this.currJarID++);
            int nextJarID = this.idMap.get(jarPath);
            // this line creates the fact in asp language syntax
            this.factsBuilder.append("\njar(").append(nextJarID).append(",\"").append(groupID).append("\",\"").append(artifactID).append("\",\"").append(version).append("\").\n");
            //Object[] classNames = JarParser.getClassNames(jarPath);
            parseMethodFact(jarPath);
        }
    }


}
