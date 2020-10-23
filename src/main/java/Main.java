import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*********************************
 Created by Fabian Oraze on 22.10.20
 *********************************/

public class Main {

    public static void main(String[] args) throws Exception {

        DependencyUpdater impl = new ImplNaive();

        // Test list with dependencies (groupIDs)
        List<String> groupIds = new ArrayList<String>();
        groupIds.add("org.junit.jupiter");
        groupIds.add("com.fasterxml.jackson.core");
        groupIds.add("org.w3c");

        List<String> artifactIds = new ArrayList<String>();
        artifactIds.add("junit-jupiter-api");
        artifactIds.add("jackson-databind");
        artifactIds.add("dom");

        List<String> urIs = impl.processDependencies(groupIds, artifactIds);
        System.out.println(urIs);


        List<Document> docs = impl.loadDocuments(urIs);
        Map<String, String> finals = impl.getVersions(docs);
        System.out.println(impl.mapToString(finals));

    }


}
