package dp.conflict.resolver.base;

import org.apache.maven.pom._4_0.Build;
import org.apache.maven.pom._4_0.Model;
import org.apache.maven.pom._4_0.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*********************************
 Created by Fabian Oraze on 17.11.20
 *********************************/

public class DPGraphCreator extends DPUpdaterBase {


    public static final String CURR_DIRECTORY = System.getProperty("user.dir");

    public DPGraphCreator(String pathToRepo) {
        super(pathToRepo);
        insertPlugin();
    }

    /**
     * private helper function which checks if the given pom file already has the needed plugin
     * if not it inserts it into the pom model object and writes it back
     */
    private void insertPlugin() {
        Model pom = getPomModel();
        boolean insert = true;

        if (pom.getBuild() != null) {
            for (Plugin p : pom.getBuild().getPlugins().getPlugin()) {
                if (p.getArtifactId().equals("depgraph-maven-plugin") && p.getGroupId().equals("com.github.ferstl"))
                    insert = false;
            }
        }
        if (insert) {
            pom.setBuild(new Build());
            pom.getBuild().setPlugins(new Build.Plugins());
            Plugin p = new Plugin();
            p.setGroupId("com.github.ferstl");
            p.setArtifactId("depgraph-maven-plugin");
            p.setVersion("3.3.0");
            pom.getBuild().getPlugins().getPlugin().add(p);
            try {
                writePom(new File(path + "pom.xml"), pom);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * creates the a json file with the depgraph plugin, which contains all nodes and edges as json objects
     * representing the dependency graph
     * file then is created in target directory of the given repo
     * @param buildOutputStream optional output for the stream generated while process builds, otherwise null
     */
    public void getDPJson(PrintStream buildOutputStream) {
        /*
        dot -Tpng target/dependency-graph.dot -o graph.png
        mvn depgraph:graph -DshowConflicts -DshowVersions -DgraphFormat=json
        */
        String cmd = "mvn depgraph:graph -DshowConflicts -DshowVersions -DgraphFormat=json; cat target/dependency-graph.json > " + CURR_DIRECTORY + "/src/main/resources/graph.json";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (buildOutputStream == null) {
            buildOutputStream = new PrintStream(outputStream);
        }

        ProcessBuilder pb;

        pb = getProcessBuilder(cmd);

        Process p = null;
        try {
            p = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("  Waiting for the build to end... ");

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream()));

        String content = "";
        List<String> lines = new ArrayList<>();
        String line = "";
        while (true) {
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            lines.add(line);
            content = content + line + System.getProperty("line.separator");
            if (buildOutputStream != null) {
                buildOutputStream.println(line);
                String finalLine = line;
            }
        }
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.buildOutput = outputStream.toString(StandardCharsets.UTF_8);
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        buildOutputStream.flush();
    }

    /**
     * private helper function which creates a processBuilder at the target repo and a given command
     * @param cmd command which should be executed at the target repo
     * @return {@link ProcessBuilder}
     */
    @NotNull
    private ProcessBuilder getProcessBuilder(String cmd) {
        ProcessBuilder pb;
        if (System.getProperty("os.name").startsWith("Windows")) {
            pb = new ProcessBuilder("cmd.exe", "/c", "cd " + this.path + " && " + cmd);
        } else {
            pb = new ProcessBuilder("/bin/bash", "-c", "cd " + this.path + " ; " + cmd);
        }
        return pb;
    }

    /**
     * function that creates .dot file representing the dependency graph
     * and via the .dot file then creates a .png graph of the pom dependencies
     */
    public void createPNG() {
        String cmd = "mvn depgraph:graph -DshowConflicts -DshowVersions";
        for (int i = 0; i <= 1; i++) {
            ProcessBuilder pb = getProcessBuilder(cmd);
            Process p = null;
            try {
                p = pb.start();
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cmd = "dot -Tpng target/dependency-graph.dot -o graph.png ; cat graph.png > " + CURR_DIRECTORY + "/src/main/resources/graph.png";
        }
    }

    @Override
    public void updateDependencies() {

    }

    @Override
    public List<Object> getWorkingConfigurations() {
        return null;
    }
}
