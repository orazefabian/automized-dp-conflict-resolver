import dp.conflict.resolver.tree.CallTree;

import java.io.FileInputStream;
import java.util.*;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class Main {

    public static void main(String[] args) {

        String jar = "/Users/fabian/.m2/repository/javax/xml/bind/jaxb-api/2.2.3/jaxb-api-2.2.3.jar";

        String target = "/Users/fabian/Projects/Sample/runtime_conflict_sample/Project_A/";
     /*
        String target = "/Users/fabian/Projects/automized-DP-conflict-resolver/automized-dp-conflict-resolver/";
        String target = "/Users/fabian/Projects/Sample/conflict_sample/";
        String target = "/Users/fabian/Projects/Sample/commons-collections/";
        String target = "/Users/fabian/Projects/Sample/sample_project/";
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
        tree.getCallTree();
        long curr = (System.currentTimeMillis() - time) / 1000 / 60;

    }


    public static List getClasseNames(String jarName) {
        ArrayList classes = new ArrayList();

        try {
            JarInputStream jarFile = new JarInputStream(new FileInputStream(
                    jarName));
            JarEntry jarEntry;

            while (true) {
                jarEntry = jarFile.getNextJarEntry();
                if (jarEntry == null) {
                    break;
                }
                if (jarEntry.getName().endsWith(".class")) {

                    System.out.println("Found "
                            + jarEntry.getName().replaceAll("/", "\\."));
                    classes.add(jarEntry.getName().replaceAll("/", "\\."));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

}
