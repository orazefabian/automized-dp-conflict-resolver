import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Document;
import javax.xml.transform.TransformerException;
import java.util.List;

/*********************************
 * Created by Fabian Oraze on 22.10.20
 *********************************
 *
 * interface responsible for getting the newest versions of given dependencies
 */
public interface DependencyUpdater {


    /**
     * Static URL that points to the central maven repository
     */
    String url = "https://repo1.maven.org/maven2/";


    /**
     * Postfix for each URL to get to proper xml file with the version data
     */
    String metaData = "/maven-metadata.xml";


    /**
     * Abstract method which should ultimately concatenate the dependencies with the prefix of the static URL
     * @param groupIds a List containing Strings of the used dependencies
     * @param artifactIds a List with the corresponding artifactId to each groupId
     * @return List with the processed dps regarding their group and artifact id
     */
    List<String> processDependencies(List<String> groupIds, List<String> artifactIds);


    /**
     * Function that loads a xml document from a URL
     * @param urls a List with URLs of xml files
     * @return a List with Document objects created by the corresponding URL
     * @throws Exception if document can not be properly loaded with a given url
     */
    List<Document> loadDocuments(List<String> urls) throws Exception;


    /**
     * Should transform and display a given Document to the console for debugging/visualizing purposes
     * @param doc Document object containing the XML
     * @throws TransformerException if Document could not be properly transformed
     */
    void displayDOM(Document doc) throws TransformerException;


    /**
     * Gets the newest versions of a dp from the given XML in form of a Document object
     * @param docs List of Document objects, each containing the XML for a dp
     * @return List of Strings with the newest version for each dp
     */
    List<String> getVersions(List<Document> docs);
}
