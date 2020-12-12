import dp.conflict.resolver.tree.CallTree;

public class Main {

    public static void main(String[] args) {

        String jar = "/Users/fabian/.m2/repository/javax/xml/bind/jaxb-api/2.2.3/jaxb-api-2.2.3.jar";

        String target = "/Users/fabian/Projects/Sample/runtime_conflict_sample/Project_A/";
     /*
        String target = "/Users/fabian/Projects/automized-DP-conflict-resolver/automized-dp-conflict-resolver/";
        String target = "/Users/fabian/Projects/Sample/conflict_sample/";
        String target = "/Users/fabian/Projects/Sample/sample_project/";
        String target = "/Users/fabian/Projects/Sample/commons-collections/";
        String jar = "/Users/fabian/.m2/repository/org/example/Project_A/1.0/Project_A-1.0.jar";

        dp.DPUpdaterBase impl = new dp.ImplNaive(sample, 2);

        impl.updateDependencies();
        System.out.println(impl.getWorkingConfigurations());
    */

    /*
        dp.DPGraphCreator cf = new dp.DPGraphCreator(target);
        cf.getDPJson(null);
        cf.createPNG();
    */


        long time = System.currentTimeMillis();
        CallTree tree = null;
        try {
            tree = new CallTree(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tree.computeCallTree();
        tree.getConflicts();
        long curr = (System.currentTimeMillis() - time) / 1000 / 60;

    }


}
