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

/*********************************
 Created by Fabian Oraze on 22.10.20
 *********************************
 *
 * Implementation of the DependencyUpdater interface
 * Naive approach which just gets the latest versions of a provided List with the groupIDs of a number of dependencies
 *
 */
public class ImplNaive implements DependencyUpdater {


    /**
     * implementation which computes full URLs to then access the metadata xml file for all dependencies
     *
     * @param groupIds a List containing Strings of the used dependencies
     * @return List with the final URLs
     */
    public List<String> processDependencies(List<String> groupIds, List<String> artifactIds) {
        List<String> finals = new ArrayList<String>();
        for (int i = 0; i < groupIds.size(); i++) {
            String gid = groupIds.get(i);
            String aid = artifactIds.get(i);
            finals.add(DependencyUpdater.url + replaceDot(gid) + "/" + aid + getPostfix());
        }
        return finals;
    }


    /**
     * Helper function to replace the '.' in each dp to '/'
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
        return  DependencyUpdater.metaData;
    }


    /**
     * implementation for document loader
     *
     * @param urls List with final URLs pointing towards meta-data.xml on central maven repository
     * @return a List with Documents gotten from the connection established via the URL
     * @throws Exception when parsing via the connection through the url fails
     */
    public List<Document> loadDocuments(List<String> urls) throws Exception {
        List<Document> documents = new ArrayList<Document>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        for (String s : urls) {
            URLConnection con = new URL(s).openConnection();
            documents.add(factory.newDocumentBuilder().parse(con.getInputStream()));
        }
        return documents;
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
     * implementation of the abstract method which
     *
     * @param docs List of Document objects, each containing the XML for a dp
     * @return List with the final string representation of the latest versions of each dp
     */
    public List<String> getVersions(List<Document> docs) {
        List<String> dpNewest = new ArrayList<String>();
        for (Document doc : docs) {
            NodeList groupId = doc.getElementsByTagName("groupId");
            NodeList list = doc.getElementsByTagName("latest");
            dpNewest.add("\n" + "Latest version: [" + list.item(0).getTextContent() + "] for dependency: \t" + groupId.item(0).getTextContent());
        }
        return dpNewest;
    }
}
