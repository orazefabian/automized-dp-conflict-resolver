import com.strobel.decompiler.ast.Node;
import dp.conflict.resolver.parse.FactParser;
import dp.conflict.resolver.parse.JarParser;
import dp.conflict.resolver.tree.CallTree;
import dp.conflict.resolver.tree.ConflictType;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import spoon.JarLauncher;
import spoon.MavenLauncher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtReference;
import spoon.reflect.visitor.filter.FieldAccessFilter;
import spoon.reflect.visitor.filter.TypeFilter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {

    public static void main(String[] args) {

        String test = "/Users/fabian/Projects/Sample/runtime_conflict_sample/Project_A/";
        String target = "/Users/fabian/Projects/Sample/";
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

        /*try {
            System.out.println(JarParser.parseJarContent("/Users/fabian/.m2/repository/org/runtime/conflict/Project_D/2.0/Project_D-2.0.jar", "conflict/Object_D"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
        */

        long time = System.currentTimeMillis();
        CallTree tree = null;
        try {
            tree = new CallTree(test);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tree.computeCallTree();
        long currTime = (System.currentTimeMillis() - time) / 1000 / 60;
        try {
            FactParser parser = new FactParser(tree.getConflicts(ConflictType.TYPE_2));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }


}
