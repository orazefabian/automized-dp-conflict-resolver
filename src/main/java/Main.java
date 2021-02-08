import dp.resolver.tree.AnswerObject;
import dp.resolver.parse.FactBuilder;
import dp.resolver.parse.exception.NoConflictException;
import dp.resolver.tree.CallTree;
import dp.resolver.tree.ConflictType;
import dp.resolver.tree.Tree;

import java.io.IOException;


public class Main {

    public static void main(String[] args) {

        String test = "/Users/fabian/Projects/Sample/runtime_conflict_sample/Project_A/";
        String target = "/Users/fabian/Projects/Sample/fastjson/";
        String curr = "/Users/fabian/Projects/automized-DP-conflict-resolver/automized-dp-conflict-resolver/";
        String jar = "/Users/fabian/.m2/repository/org/runtime/conflict/Project_B/1.0/Project_B-1.0.jar";
     /*
        String target = "/Users/fabian/Projects/Sample/commons-collections/";
        String target = "/Users/fabian/Projects/Sample/sample_project/";
        String target = "/Users/fabian/Projects/Sample/conflict_sample/";
    */

    /*
        dp.DPUpdaterBase impl = new dp.ImplNaive(sample, 2);

        impl.updateDependencies();
        System.out.println(impl.getWorkingConfigurations());
        dp.DPGraphCreator cf = new dp.DPGraphCreator(target);
        cf.getDPJson(null);
        cf.createPNG();
    */

        /*List<ClazzWithMethodsDto> jarClassList = AssistParser.getJarClassList(jar);
        System.out.println(jarClassList.toString());

        System.exit(0);*/

        long time = System.currentTimeMillis();
        Tree tree = null;
        AnswerObject answer = new AnswerObject();
        try {
            tree = new CallTree(test, answer);
            tree.computeCallTree();
            FactBuilder parser;
            parser = new FactBuilder(tree.getConflicts(ConflictType.TYPE_1));
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
