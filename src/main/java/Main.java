import java.util.ArrayList;
import java.util.List;

/*********************************
 Created by Fabian Oraze on 22.10.20
 *********************************/

public class Main {

    public static void main(String[] args) {

        ImplTest impl = new ImplTest();

        // Test list with dependencies, groupID
        List<String> dps = new ArrayList();
        dps.add("org/junit/jupiter");
        dps.add("com/fasterxml/jackson/core");

        System.out.println(impl.getURIs(dps));

    }

}
