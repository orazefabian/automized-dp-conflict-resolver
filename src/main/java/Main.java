import dp.conflict.resolver.parse.AnswerObject;
import dp.conflict.resolver.parse.FactParser;
import dp.conflict.resolver.parse.NoConflictException;
import dp.conflict.resolver.tree.CallTree;
import dp.conflict.resolver.tree.ConflictType;

import java.io.IOException;


public class Main {

    public static void main(String[] args) {

        String test = "/Users/fabian/Projects/Sample/runtime_conflict_sample/Project_A/";
        String target = "/Users/fabian/Projects/Sample/fastjson/";
        String curr = "/Users/fabian/Projects/automized-DP-conflict-resolver/automized-dp-conflict-resolver/";
     /*
        String target = "/Users/fabian/Projects/Sample/commons-collections/";
        String target = "/Users/fabian/Projects/Sample/sample_project/";
        String target = "/Users/fabian/Projects/Sample/conflict_sample/";
        String jar = "/Users/fabian/.m2/repository/org/example/Project_A/1.0/Project_A-1.0.jar";
    */

    /*
        dp.DPUpdaterBase impl = new dp.ImplNaive(sample, 2);

        impl.updateDependencies();
        System.out.println(impl.getWorkingConfigurations());
        dp.DPGraphCreator cf = new dp.DPGraphCreator(target);
        cf.getDPJson(null);
        cf.createPNG();
    */


        long time = System.currentTimeMillis();
        CallTree tree = null;
        AnswerObject answer = new AnswerObject();
        try {
            tree = new CallTree(test, answer);
            tree.computeCallTree();
            FactParser parser;
            parser = new FactParser(tree.getConflicts(ConflictType.TYPE_2));
            answer.setIDMap(parser.getIdMap());
            answer.solve();
            long currTime = (System.currentTimeMillis() - time) / 1000 / 60;
            System.out.println("Needed time: " + currTime + " min");
            System.out.println("Possible jar configurations: "+ answer.getAnswers());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (NoConflictException e) {
            System.err.println("No conflicts to solve");
        } finally {
            System.out.println("Bloated jars: " + answer.getBloatedJars());
        }


    }


}
