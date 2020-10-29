import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.net.URL;
import java.net.URLConnection;
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

}
