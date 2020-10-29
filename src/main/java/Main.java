import api.github.json.POMJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.maven.pom._4_0.Model;
import org.w3c.dom.Document;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.io.*;
import java.net.URL;
import java.util.*;

/*********************************
 Created by Fabian Oraze on 22.10.20
 *********************************/

public class Main {

    public static void main(String[] args) throws Exception {


        // Test list with dependencies (groupIDs)
        List<String> groupIds = new ArrayList<>();
        groupIds.add("org.junit.jupiter");
        groupIds.add("com.fasterxml.jackson.core");
        groupIds.add("org.w3c");

        List<String> artifactIds = new ArrayList<>();
        artifactIds.add("junit-jupiter-api");
        artifactIds.add("jackson-databind");
        artifactIds.add("dom");

        DPUpdaterBase impl = new ImplNaive();
        String urIs = impl.processDependencies(groupIds.get(0), artifactIds.get(0));
        Document docs = impl.loadDocument(urIs);
        List<String> finals = impl.getVersions(docs);
        System.out.println(finals);
        System.out.println(urIs);



        XmlMapper mapper = new XmlMapper();
        mapper.setDefaultUseWrapper(false);
        String query = "https://api.github.com/repos/spotify/docker-maven-plugin/contents/pom.xml";
        URL url = new URL(query);
        System.out.println(url);

        ObjectMapper objectMapper = new ObjectMapper();
        POMJson pomJson = objectMapper.readValue(url, POMJson.class);

        System.out.println(pomJson.getContent());

        Base64.Decoder decoder = Base64.getDecoder();



        // Model model = mapper.readValue(pomJson.getContent(), Model.class);


        /*DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        URLConnection con = url.openConnection();
        Document doc = factory.newDocumentBuilder().parse(con.getInputStream());
        impl.displayDOM(doc);*/

        /*File pom = new File("pom.xml");
        Model mdl = mapper.readValue(url, Model.class);

        String xml = mapper.writeValueAsString(mdl);
        File file = new File("new.txt");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(xml);
        fileWriter.close();

        Model.Dependencies dps = mdl.getDependencies();
        List<Dependency> dp = dps.getDependency();
        for (Dependency d : dp) {
            System.out.println(d.getGroupId() + " " + d.getArtifactId() + "\n");
        }*/
        //printPom(pom, mdl);

        /*PrintStream stream = new PrintStream("new.txt");

        buildProject(null, stream, null);

        System.out.println(stream);
        */
    }

    protected static void printPom(File file, Model model) throws JAXBException {
//        LOG.info("Updating pom: "+file.getAbsolutePath());
        JAXBContext jaxbContext = JAXBContext.newInstance(Model.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

//        jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd%22);
        jaxbMarshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd%22");

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

//        new QName()
        jaxbMarshaller.marshal(new JAXBElement<Model>(new QName("http://maven.apache.org/POM/4.0.0%22,%22project%22,%22%22"), Model.class, model), file);
//        jaxbMarshaller.marshal(model, file);
//        jaxbMarshaller.marshal(model, System.out);
    }

    public static void buildProject(File repoFolder, PrintStream buildOutputStream, String cmd) throws IOException, InterruptedException {
        // FixSummary.put("buildStart",LocalDateTime.now());
        if (cmd == null) {
            cmd = "mvn -U -DskipTests=true clean package";
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
                System.out.println(line);
                //listener //Refactor that only listeners get called here (and make a listener for the print stream
                String finalLine = line;
                // this.repairListeners.forEach(x->x.newBuildLine(finalLine));
            }
        }

        p.waitFor();
        System.out.println("  Build ended...");
        //        LOG.info("Finished waiting...");

        //MavenLogAnalyzer mla = new MavenLogAnalyzer();
        //FixSummary.put("buildEnd",LocalDateTime.now());
        //FixSummary.put("mlaStart",LocalDateTime.now());
        //BuildLog buildLog = mla.analyzeMavenLog(content, lines);
        //FixSummary.put("mlaEnd",LocalDateTime.now());
        //return buildLog;
    }
}
