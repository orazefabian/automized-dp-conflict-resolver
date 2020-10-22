/*********************************
 Created by Fabian Oraze on 22.10.20
 *********************************/

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
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
     * Jackson object to get data from a given URI
     */
    ObjectMapper mapper = new ObjectMapper();

    /**
     * Abstract method which should ultimately concatenate the dependencies with the prefix of the static URL
     * @param dps a List containing Strings of the used dependencies
     * @return List with the final URIs for the {@link ObjectMapper}
     */
    List<String> getURIs(List<String> dps);


}
