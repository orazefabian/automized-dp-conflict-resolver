import dp.api.maven.CentralMavenAPI;
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

        String test = "/Users/fabian/Projects/Sample/runtime_conflict_sample/Project_A/";
        String target = "/Users/fabian/Projects/Sample/fastjson/";
        String curr = "/Users/fabian/Projects/automized-DP-conflict-resolver/automized-dp-conflict-resolver/";
        String jar = "/Users/fabian/.m2/repository/org/runtime/conflict/Project_B/1.0/Project_B-1.0.jar";

        //String param = args[0];


        long time = System.currentTimeMillis();
        Tree tree;
        AnswerObject answer = new AnswerObject();
        CentralMavenAPI.setMaxVersionsNumFromCmr(5);
        try {
            tree = new CallTree(curr, answer);
            tree.computeCallTree();
            FactBuilder parser;
            parser = new FactBuilder(tree.getConflicts(ConflictType.TYPE_3), tree.getNeededJars());
            answer.setIDMap(parser.getIdMap());
            answer.solve();
            long currTime = (System.currentTimeMillis() - time) / 1000 / 60;
            System.out.println("Needed time: " + currTime + " min");
            System.out.println("Possible jar configurations: " + answer.getAnswers());

            File output = new File("target/output.txt");
            output.createNewFile();
            FileWriter writer = new FileWriter(output);
            for (List<String> configuration : answer.getAnswers()) {
                writer.write("Possible dependency configuration:\n");
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


}
