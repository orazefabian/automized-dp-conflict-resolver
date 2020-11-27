package spoon;

import dp.DPUpdaterBase;
import org.apache.maven.pom._4_0.Dependency;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/*********************************
 Created by Fabian Oraze on 27.11.20
 *********************************/

public class SpoonImpl {

    private DPUpdaterBase base;
    private MavenLauncher baseMVNProject;
    private List<JarLauncher> jars;
    private CtModel ctModel;

    public SpoonImpl(String pathToProject) {
        this.baseMVNProject = new MavenLauncher(pathToProject, MavenLauncher.SOURCE_TYPE.ALL_SOURCE);
        this.baseMVNProject.getEnvironment().setAutoImports(true);
        ctModel = this.baseMVNProject.buildModel();

        this.jars = new ArrayList<>();

        this.base = new DPUpdaterBase(pathToProject) {
            @Override
            public void updateDependencies() {
            }

            @Override
            public List<Object> getWorkingConfigurations() {
                return null;
            }
        };
    }

    public List<CtMethod> iterateMethods(CtModel ctModel) {
        List<CtMethod> methods = new ArrayList<>();
        for (CtType<?> s : ctModel.getAllTypes()) {
            System.out.println("class: " + s.getQualifiedName());
            for (CtMethod<?> m : s.getAllMethods()) {
                methods.add(m);
            }
        }
        return methods;
    }

    public List<String> getJarPaths() {

        for (Dependency dp : this.base.getPomModel().getDependencies().getDependency()) {

        }


        return null;
    }

    public void searchInvocation(CtMethod method, CtModel model) {

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
