import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.maven.pom._4_0.Model;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    protected final static JacksonXmlModule XML_MODULE = new JacksonXmlModule();
    protected final String path;
    protected String buildOutput;
    protected File repo;
    protected Model pomModel;
    protected List<String> versions;
    protected ObjectMapper mapper;

    public DPUpdaterBase(String pathToRepo) throws IOException {
        this.repo = new File(pathToRepo);
        this.path = pathToRepo;
        this.versions = new ArrayList<>();
        this.pomModel = createPomModel(pathToRepo);
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
     * @param url List with final URLs pointing towards meta-data.xml on central maven repository
     * @return a List with Documents gotten from the connection established via the URL
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
    private String replaceDot(String s) {
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
     * @param doc Document object containing XML
     * @throws TransformerException when TransformerFactory fails to transform the document to the StreamResult
     */
    public void displayDOM(Document doc) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer xForm = transformerFactory.newTransformer();

        xForm.transform(new DOMSource(doc), new StreamResult(System.out));
    }


    /**
     * @param doc Document object, containing the metadata XML for a dp
     * @return HashMap with the groupId , latest version pairs for each dp
     */
    public List<String> getVersions(Document doc) {
        List<String> versions = new ArrayList<>();

        NodeList list = doc.getElementsByTagName("versions");
        for (int i = 0; i < list.getLength(); i++) {
            versions.add(list.item(i).getTextContent());
        }
        return versions;
    }

    /**
     * ToString function to represent final map of groupId version pairs
     *
     * @param map containing groupIds as a key and the corresponding latest version as values
     * @return String representation for better readability
     */
    public String mapToString(Map<String, String> map) {
        StringBuilder builder = new StringBuilder();
        for (String gid : map.keySet()) {
            builder.append("\nDependency: \t\t").append(gid).append("\nLatest version: \t").append(map.get(gid)).append("\n-------------------------------------------------------------");
        }
        return builder.toString();
    }

    /**
     * should write a pom.xml file given a model object representation
     * @param file output file which the model should be written to
     * @param model {@link Model} object which is a java representation of a pom file
     * @throws JAXBException when marshalling fails
     */
    @Override
    public void writePom(File file, Model model) throws JAXBException {
        // LOG.info("Updating pom: "+file.getAbsolutePath());
        JAXBContext jaxbContext = JAXBContext.newInstance(Model.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd%22);
        jaxbMarshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd%22");

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // new QName()
        jaxbMarshaller.marshal(new JAXBElement<>(new QName("project"), Model.class, model), file);
        //jaxbMarshaller.marshal(model, file);
        // jaxbMarshaller.marshal(model, System.out);
    }

    @Override
    public Model getPomModel() {
        return this.pomModel;
    }

    @Override
    public Model createPomModel(String repoPath) throws IOException {
        File xml = new File(repoPath + "pom.xml");
        XML_MODULE.setDefaultUseWrapper(false);
        this.mapper = new XmlMapper(XML_MODULE);
        return mapper.readValue(xml, Model.class);
    }

    @Override
    public void buildProject(File repoFolder, PrintStream buildOutputStream, String cmd) throws IOException, InterruptedException {
            // FixSummary.put("buildStart",LocalDateTime.now());
            if (cmd == null) {
                cmd = "mvn -U -DskipTests=true clean package";
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
}
