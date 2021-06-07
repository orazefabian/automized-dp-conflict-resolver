import dp.api.maven.CentralMavenAPI;
import dp.resolver.base.ImplSpoon;
import dp.resolver.tree.AnswerObject;
import dp.resolver.parse.FactBuilder;
import dp.resolver.parse.exception.NoConflictException;
import dp.resolver.tree.CallTree;
import dp.resolver.tree.ConflictType;
import dp.resolver.tree.Tree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class Main {


    public static void main(String[] args) {
        int MAX_VERSIONS_NUM = 5;
        int POM_LIMIT = 5;

        if (args.length == 0 || args[0].startsWith("-Xmx")) {
            printHelp();
            System.exit(0);
        }

        String param = args[0];


        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith("-m")) {
                MAX_VERSIONS_NUM = Integer.parseInt(args[i].split("-m")[1]);
            } else if (args[i].startsWith("-p")) {
                POM_LIMIT = Integer.parseInt(args[i].split("-p")[1]);
            }
        }

        param = "/Users/fabian/Projects/automized-DP-conflict-resolver/automized-dp-conflict-resolver";
        String wicket = "/Users/fabian/Projects/Github_sample_repos/wicket-core/wicket-core-9.3.0.jar";

        long time = System.currentTimeMillis();
        Tree tree;
        AnswerObject answer = new AnswerObject();
        CentralMavenAPI.setMaxVersionsNumFromCmr(MAX_VERSIONS_NUM);
        try {
            File output = new File("output.txt");
            output.createNewFile();
            FileWriter writer = new FileWriter(output);

            tree = new CallTree(param, answer);
            tree.computeCallTree();
            FactBuilder parser;
            parser = new FactBuilder(tree.getConflicts(ConflictType.TYPE_3), tree.getNeededJars());
            answer.setIDMap(parser.getIdMap());
            answer.solve();
            long currTime = (System.currentTimeMillis() - time) / 1000 / 60;
            String timeInfo = "Needed time: " + currTime + " min";
            System.out.println("Possible jar configurations: " + answer.getAnswers());
            System.out.println(timeInfo);

            ImplSpoon pomOld = new ImplSpoon(param);
            pomOld.updateAndWritePomModels(answer.getAnswers(), POM_LIMIT);

            writer.write(timeInfo + "\n");

            writer.write("\nCould not find usage for following dependencies: (bloated)\n");
            for (String bloated : answer.getBloatedJars()) {
                writer.write(bloated + "\n");
            }

            for (List<String> configuration : answer.getAnswers()) {
                writer.write("\nPossible dependency configuration:\n");
                for (String dependency : configuration) {
                    writer.write(dependency + "\n");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (NoConflictException e) {
            System.err.println("No conflicts to solve");
        } finally {
            System.out.println("Bloated jars: " + answer.getBloatedJars());

        }


    }

    private static void printHelp() {
        StringBuilder builder = new StringBuilder();
        builder.append("Usage: java -cp acr.jar Main [path-to-project] [-m<max-maven-downloads>] [-p<max-pom-generations>] [max-heap-space]\n");
        builder.append("\n  path-to-project:        full path to the project to be analyzed (has to be specified)");
        builder.append("\n  max-maven-downloads:    upper limit of newest jar version to be downloaded during the process (default = 5)");
        builder.append("\n  max-pom-generations:    max number of poms generated as a solution output (default = 5)");
        builder.append("\n  max-heap-space:         max heap space available to the jvm, schema: [-Xmx<space>] where space is e.g. 8g or 16g");
        System.out.println(builder.toString());
    }


}
