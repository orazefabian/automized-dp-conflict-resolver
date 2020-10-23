import org.w3c.dom.Document;
import java.util.ArrayList;
import java.util.List;

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

        List<String> urIs = impl.getURIs(groupIds, artifactIds);
        System.out.println(urIs);


        List<Document> docs = impl.loadDocuments(urIs);
        List<String> versions = impl.getVersions(docs);
        System.out.println(versions);

    }


}
