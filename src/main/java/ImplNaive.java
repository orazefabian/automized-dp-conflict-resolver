import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/*********************************
 Created by Fabian Oraze on 22.10.20
 *********************************/

public class ImplNaive implements DependencyUpdater {


    /**
     * implementation which computes full URLs to then access the metadata xml file for all dependencies
     * @param dps a List containing Strings of the used dependencies
     * @return List with the final URLs
     */
    public List<String> getURIs(List<String> dps) {
        List<String> finals = new ArrayList<String>();
        for (int i = 0; i < dps.size(); i++) {
            String dp = dps.get(i);
            finals.add(DependencyUpdater.url + dp + getPostfix(dp) + DependencyUpdater.metaData);
        }
        return finals;

    }

    /**
     * Helper function to get the proper postfix to a given dependency
     * @param url String from pom.xml file with the group id of the dependency
     * @return the correct postfix with '-' instead of '/'
     */
    private String getPostfix(String url) {
        String[] subStr = url.split("/");
        int len = subStr.length;
        String postfix = "/" + subStr[len - 2] + "-" + subStr[len - 1];
        return postfix;
    }


    /**
     * implementation for document loader
     * @param url final URL pointing towards meta-data.xml on central maven repository
     * @return a Document gotten from the connection established via the URL
     * @throws Exception
     */
    public Document loadDocument(String url) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        URLConnection con = new URL(url).openConnection();
        return factory.newDocumentBuilder().parse(con.getInputStream());
    }



}
