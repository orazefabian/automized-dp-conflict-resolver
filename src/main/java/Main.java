
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.ArrayList;
import java.util.List;

/*********************************
 Created by Fabian Oraze on 22.10.20
 *********************************/

public class Main {

    public static void main(String[] args) throws Exception {

        DependencyUpdater impl = new ImplNaive();

        // Test list with dependencies, groupID
        List<String> dps = new ArrayList();
        dps.add("org/junit/jupiter");
        dps.add("com/fasterxml/jackson/core");

        List<String> urIs = impl.getURIs(dps);
        System.out.println(urIs);


        Document doc = impl.loadDocument(urIs.get(0));


        TransformerFactory transformerFactory= TransformerFactory.newInstance();
        Transformer xForm = transformerFactory.newTransformer();

        xForm.transform(new DOMSource(doc), new StreamResult(System.out));

    }





}
