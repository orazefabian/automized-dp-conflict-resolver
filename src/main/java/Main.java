import java.io.*;

/*********************************
 Created by Fabian Oraze on 22.10.20
 *********************************/

public class Main {

    public static void main(String[] args) throws Exception {


        // Test list with dependencies (groupIDs)
        /*List<String> groupIds = new ArrayList<>();
        groupIds.add("org.junit.jupiter");
        groupIds.add("com.fasterxml.jackson.core");
        groupIds.add("org.w3c");

        List<String> artifactIds = new ArrayList<>();
        artifactIds.add("junit-jupiter-api");
        artifactIds.add("jackson-databind");
        artifactIds.add("dom");*/

        String repo = "/Users/fabian/Projects/Sample/sample_project/";

        DPUpdaterBase impl = new ImplNaive(repo);

        impl.getBuildSuccess(false);

        impl.computeVersionConfiguration();

        impl.writePom(new File("/Users/fabian/Projects/Sample/pom.xml"), impl.getPomModel());


        /*PrintStream stream = new PrintStream("new.txt");

        impl.buildProject(null, stream, null);

        System.out.println(stream);*/
    }




}
