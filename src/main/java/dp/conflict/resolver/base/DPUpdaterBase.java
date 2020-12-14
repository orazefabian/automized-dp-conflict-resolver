package dp.conflict.resolver.base;

import org.apache.maven.pom._4_0.Dependency;
import org.apache.maven.pom._4_0.Model;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*********************************
 Created by Fabian Oraze on 29.10.20
 ********************************
 *
 * Base DP Updater class which contains variables and methods to obtain the versions of
 * a given groupID and Artifact ID
 *
 * This process is always necessary as a preprocessor before the version compatibilities can be tested
 * */

public abstract class DPUpdaterBase implements DPUpdater {

    /**
     * Static URL that points to the central maven repository
     */
    public static final String url = "https://repo1.maven.org/maven2/";
    /**
     * Postfix for each URL to get to proper xml file with the version data
     */
    public static final String metaData = "/maven-metadata.xml";

    protected final String path;
    protected String buildOutput;
    protected File repo;
    protected Model pomModel;
    protected List<ArrayList<String>> dpVersionList;


    /**
     * base constructor for abstract updater class
     * initializes a new {@link File} given a String parameter, a java object {@link Model} representing
     * the pom.xml of the repo and creates a empty list of lists for later storing of the versions for each dependency
     *
     * @param pathToRepo String pointing to the root repo directory
     */
    public DPUpdaterBase(String pathToRepo) {
        this.repo = new File(pathToRepo);
        this.path = pathToRepo;
        this.pomModel = createPomModel(pathToRepo);
        this.dpVersionList = new ArrayList<ArrayList<String>>();
        try {
            saveDependencies();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * implementation which computes full URLs to then access the metadata xml file for all dependencies
     *
     * @param groupIds a List containing Strings of the used dependencies
     * @return List with the final URLs
     */
    public String processDependencies(String groupIds, String artifactIds) {
        return (url + replaceDot(groupIds) + "/" + artifactIds + getPostfix());
    }

    /**
     * implementation for document loader
     *
     * @param url final URLs pointing towards meta-data.xml on central maven repository
     * @return a Document gotten from the connection established via the URL
     * @throws Exception when parsing via the connection through the url fails
     */
    public Document loadDocument(String url) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        URLConnection con = new URL(url).openConnection();
        return (factory.newDocumentBuilder().parse(con.getInputStream()));
    }

    /**
     * Helper function to replace the '.' in each dp to '/'
     *
     * @param s groupId
     * @return replaced version of the groupId with '/' in between
     */
    private String replaceDot(@NotNull String s) {
        return s.replace('.', '/');
    }

    /**
     * Helper function to get the proper postfix to access meta-data.xml
     *
     * @return the correct postfix with metadata.xml
     */
    private String getPostfix() {
        return metaData;
    }

    /**
     * implementation that transforms a Document object with the transformer class and and prints it to the System.out
     *
     * @param url String containing the url to the xml document
     * @throws TransformerException when TransformerFactory fails to transform the document to the StreamResult
     */
    public void displayDOM(String url) throws TransformerException {
        Document doc = null;
        try {
            doc = loadDocument(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer xForm = transformerFactory.newTransformer();

        xForm.transform(new DOMSource(doc), new StreamResult(System.out));
    }


    /**
     * @param doc Document object, containing the metadata XML for a dp
     * @return List with all versions available for a given metadata.xml for a single dp
     */
    public List<String> getVersions(@NotNull Document doc) {
        List<String> versions = new ArrayList<>();

        NodeList list = doc.getElementsByTagName("version");
        for (int i = 0; i < list.getLength(); i++) {
            versions.add(list.item(i).getTextContent());
        }
        return versions;
    }

    /**
     * should write a pom.xml file given a model object representation
     *
     * @param file  output file which the model should be written to
     * @param model {@link Model} object which is a java representation of a pom file
     * @throws JAXBException when marshalling fails
     */
    @Override
    public void writePom(File file, Model model) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Model.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd");

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(new JAXBElement<Model>(new QName("http://maven.apache.org/POM/4.0.0", "project", ""), Model.class, model), file);
        // jaxbMarshaller.marshal(model, file);
        // jaxbMarshaller.marshal(model, System.out);
    }

    /**
     * @return object {@link Model} representation of the pom.xml file
     */
    @Override
    public Model getPomModel() {
        return this.pomModel;
    }

    /**
     * parses the pom.xml to a java object with the
     *
     * @param repoPath String with the absolute path to a maven repo folder
     * @return object {@link Model}
     */
    @Override
    public Model createPomModel(String repoPath) {
//        LOG.info("Creating POM Model "+pomFile.getAbsolutePath());
        try {
            File pomFile;
            JAXBContext jc = JAXBContext.newInstance(Model.class);
            if (repoPath.endsWith(".jar")) {
                pomFile = new File(repoPath.replace(".jar", ".pom"));
            } else {
                pomFile = new File(repoPath + "pom.xml");
            }
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            JAXBElement<Model> feed = unmarshaller.unmarshal(new StreamSource(new FileInputStream(pomFile)), Model.class);

//            LOG.info("Creating POM Model finished");
            return feed.getValue();
        } catch (Exception e) { //currently we do nothing!
            e.printStackTrace();
//            LOG.info("Could not generate pomModel: "+ e.getMessage());
//            LOG.info("  "+pomFile.getAbsolutePath());
//            LOG.error("",e);
//            LOG.info("  Creating POM Model finished with error "+pomFile.getAbsolutePath());
//            LOG.info("  "+e.getMessage());
        }
        return null;
    }


    /**
     * starts a process that builds the current pom.xml file of the repo folder and saves the build log to a class variable
     *
     * @param repoFolder        the root folder which contains the pom
     * @param buildOutputStream a {@link PrintStream} which can be specified to contain the log of the process
     * @param cmd               additional command e.d. "maven -U clean package"
     * @throws IOException          when reading the repoFolder fails
     * @throws InterruptedException when process is interrupted
     */
    @Override
    public void buildProject(File repoFolder, PrintStream buildOutputStream, String cmd) throws IOException, InterruptedException {
        // FixSummary.put("buildStart",LocalDateTime.now());
        if (cmd == null) {
            cmd = "mvn -U -Drat.ignoreErrors=true clean package";
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (buildOutputStream == null) {
            buildOutputStream = new PrintStream(outputStream);
        }

        ProcessBuilder pb;

        if (System.getProperty("os.name").startsWith("Windows")) {
            pb = new ProcessBuilder("cmd.exe", "/c", "cd " + repoFolder + " && " + cmd);
        } else {
            pb = new ProcessBuilder("/bin/bash", "-c", "cd " + repoFolder + " ; " + cmd);
        }

        Process p = pb.start();
        System.out.println("  Waiting for the build to end... ");

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream()));

        String content = "";
        List<String> lines = new ArrayList<>();
        String line = "";
        while ((line = reader.readLine()) != null) {
            lines.add(line);
            content = content + line + System.getProperty("line.separator");
            if (buildOutputStream != null) {
                buildOutputStream.println(line);
                //listener //Refactor that only listeners get called here (and make a listener for the print stream
                String finalLine = line;
                // this.repairListeners.forEach(x->x.newBuildLine(finalLine));
            }
        }
        p.waitFor();
        buildOutput = outputStream.toString(StandardCharsets.UTF_8);
        outputStream.flush();
        buildOutputStream.flush();
        System.out.println("  Build ended...");

        //LOG.info("Finished waiting...");
        //MavenLogAnalyzer mla = new MavenLogAnalyzer();
        //FixSummary.put("buildEnd",LocalDateTime.now());
        //FixSummary.put("mlaStart",LocalDateTime.now());
        //BuildLog buildLog = mla.analyzeMavenLog(content, lines);
        //FixSummary.put("mlaEnd",LocalDateTime.now());
        //return buildLog;
    }

    /**
     * calls buildProject method and then checks the build-output whether it was successful or not via verifying if the output contains "BUILD SUCCESS"
     *
     * @param printOutput if true then the buildLog of the last build should also printed to the console
     * @return true if the last build was successful
     */
    @Override
    public boolean getBuildSuccess(boolean printOutput) {
        try {
            buildProject(repo, null, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean success = this.buildOutput.contains("BUILD SUCCESS");
        System.out.println("Build successful: " + success);
        if (printOutput) System.out.println(buildOutput);
        return success;
    }


    /**
     * saves the dependencies of the current pom object to a class variable
     * uses method processDependencies to get all versions from central maven repo
     *
     * @throws Exception if accessing url fails
     */
    @Override
    public void saveDependencies() throws Exception {
        if (this.dpVersionList.size() == 0) {
            Model.Dependencies dps = this.pomModel.getDependencies();
            List<Dependency> dp = dps.getDependency();
            Document doc;
            String url;
            List<String> versions;
            for (Dependency d : dp) {
                url = processDependencies(d.getGroupId(), d.getArtifactId());
                doc = loadDocument(url);
                versions = getVersions(doc);
                this.dpVersionList.add((ArrayList<String>) versions);
            }
        }
    }

    public File createEffectivePom(File pom) throws IOException, InterruptedException {
        System.out.println("Create Effective POM File: " + pom.getAbsolutePath());

        File baseFolder = new File(pom.getAbsolutePath().substring(0, pom.getAbsolutePath().lastIndexOf(File.separator)));
        System.out.println("BASEFOLDER: " + baseFolder.getAbsolutePath());
        File outputFile = new File(baseFolder, "effectivePom.xml");
        System.out.println("OutputFile: " + outputFile.getAbsolutePath());
        String cmd = "mvn help:effective-pom -Doutput=" + outputFile.getAbsolutePath();

        ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "cd " + baseFolder.getAbsolutePath() + " ; " + cmd);
        Process p = pb.start();

        System.out.println("  Waiting for the build to end...");
        p.waitFor();
        System.out.println(" Build ended...");
        return outputFile;
    }

    /**
     * @return list each containing a nested list of strings of all versions of the current pom model
     * Structure of the returning list containing N dependencies and Ki versions for each:
     * <p>
     * &list(
     * ["DP_1"] &list (["Version_1" , "Version_2", ... ,"Version_K1"])
     * ["DP_2"] &list (["Version_1" , "Version_2", ... ,"Version_K2"])
     * .
     * .
     * .
     * ["DP_N"] &list (["Version_1" , "Version_2", ... ,"Version_KN"])
     * )
     */
    public List<ArrayList<String>> getDpVersionList() {
        return dpVersionList;
    }

    /**
     * @return the build output of the last process
     */
    public String getBuildOutput() {
        return buildOutput;
    }
}
