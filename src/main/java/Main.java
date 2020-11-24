import org.paukov.combinatorics3.Generator;
import spoon.JarLauncher;
import spoon.Launcher;
import spoon.MavenLauncher;
import spoon.legacy.NameFilter;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtComment;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.*;
import spoon.reflect.path.CtPath;
import spoon.reflect.path.CtPathBuilder;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtIterator;
import spoon.reflect.visitor.Filter;
import spoon.reflect.visitor.filter.NamedElementFilter;
import spoon.reflect.visitor.filter.TypeFilter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        String jar = "/Users/fabian/.m2/repository/javax/xml/bind/jaxb-api/2.2.3/jaxb-api-2.2.3.jar";
        String target = "/Users/fabian/Projects/automized-DP-conflict-resolver/automized-dp-conflict-resolver";
     /*
        String target = "/Users/fabian/Projects/Sample/spring_sample/SpringMVC-Spring-MyBatis/";
        String target = "/Users/fabian/Projects/Sample/sample_project/";
        String target = "/Users/fabian/Projects/Sample/commons-collections/";
        String target = "/Users/fabian/Projects/Sample/spring_sample/SpringMVC-Spring-MyBatis/";


        DPUpdaterBase impl = new ImplNaive(sample, 2);


        impl.updateDependencies();
        System.out.println(impl.getWorkingConfigurations());
    */

    /*
        DPGraphCreator cf = new DPGraphCreator(target);

        cf.getDPJson(null);
        cf.createGraphPNG();
    */

        //JarLauncher launcher = new JarLauncher(jar);
        MavenLauncher launcher = new MavenLauncher(target, MavenLauncher.SOURCE_TYPE.ALL_SOURCE);

        launcher.getEnvironment().setAutoImports(true);

        CtModel ctModel = launcher.buildModel();


        // list all classes of the model
        for (CtType<?> s : ctModel.getAllTypes()) {
            System.out.println("class: " + s.getQualifiedName());
            for (CtMethod<?> m : s.getAllMethods()) {
                searchInvocation(m, ctModel);
            }
        }
    }

    private static void searchInvocation(CtMethod method, CtModel model) {
        List<CtInvocation> elements = method.getElements(new TypeFilter<>(CtInvocation.class));
        if (elements.size() != 0) System.out.println("method: " + method.getSignature());
        for (CtInvocation element : elements) {
            CtTypeReference declaringType = element.getExecutable().getDeclaringType();
            try {
                System.out.println("\tinvocation: " + element.getExecutable() + "\n\t\tqualified name: " + "\033[0;32m" + declaringType.getQualifiedName() + "\033[0m");
            } catch (NullPointerException e) {
                System.err.println("\t---> no qualified name for: " + element + e);
            }
            try {
                Class<?> cls = Class.forName(declaringType.getQualifiedName());
                System.out.println("\033[0;32m\t\t\tjar path: " + new File(cls.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath() + "\033[0m");
            } catch (ClassNotFoundException e) {
                System.err.println("\t---> class not found for: " + element);
            } catch (URISyntaxException e) {
                System.err.println("\t---> no uri for that element");
            } catch (NullPointerException e) {
                System.err.println("\t---> class path not found for: " + element);
            }
        }
    }

}
