import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.List;

/*********************************
 Created by Fabian Oraze on 22.10.20
 *********************************/

public class Main {

    public static void main(String[] args) throws Exception {

        DependencyUpdater impl = new ImplNaive();

        // Test list with dependencies (groupIDs)
        List<String> dps = new ArrayList<String>();
        dps.add("org/junit/jupiter");
        dps.add("com/fasterxml/jackson/core");

        List<String> urIs = impl.getURIs(dps);
        System.out.println(urIs);


        List<Document> docs = impl.loadDocuments(urIs);
        List<String> versions = impl.getVersions(docs);
        System.out.println(versions);

    }


}
