import dp.conflict.resolver.tree.CallTree;
import spoon.JarLauncher;
import spoon.MavenLauncher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class Main {

    public static void main(String[] args) {

        String jar = "/Users/fabian/.m2/repository/javax/xml/bind/jaxb-api/2.2.11/jaxb-api-2.2.11.jar";
        String target = "/Users/fabian/Projects/automized-DP-conflict-resolver/automized-dp-conflict-resolver/";
     /*
        String target = "/Users/fabian/Projects/Sample/runtime_conflict_sample/Project_A/";
        String target = "/Users/fabian/Projects/Sample/commons-collections/";
        String target = "/Users/fabian/Projects/Sample/sample_project/";
        String target = "/Users/fabian/Projects/Sample/conflict_sample/";
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

        /*JarLauncher jarLauncher = new JarLauncher(jar);
        MavenLauncher launcher = new MavenLauncher("/Users/fabian/Projects/automized-DP-conflict-resolver/automized-dp-conflict-resolver/", MavenLauncher.SOURCE_TYPE.ALL_SOURCE);

        CtModel ctModel = launcher.buildModel();

        for (Object t : ctModel.filterChildren((CtType type) -> type.getQualifiedName().contains("UpdaterBase")).list()) {
            CtType cl = (CtType) t;
            for (Object m : cl.filterChildren(new TypeFilter<>(CtMethod.class)).list()) {
                CtMethod method = (CtMethod) m;
                for (Object i : method.filterChildren(new TypeFilter<>(CtInvocation.class)).list()) {
                    CtInvocation reference = (CtInvocation) i;
                    System.out.print(reference.getExecutable());
                    try {
                        if (reference.getExecutable().getType().toString().equals("void")) {
                            System.out.println(" from not void --> " + reference.getExecutable().getDeclaringType());
                        } else {
                            System.out.println(" from --> " + reference.getExecutable().getType());
                        }
                    } catch (NullPointerException e) {
                        System.err.println("no type");
                    }
                }
            }
        }
        System.exit(0);*/

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
