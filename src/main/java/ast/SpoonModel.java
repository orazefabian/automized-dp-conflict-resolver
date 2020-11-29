package ast;

import dp.DPUpdaterBase;
import dp.ImplSpoon;
import org.apache.maven.pom._4_0.Dependency;
import spoon.JarLauncher;
import spoon.Launcher;
import spoon.MavenLauncher;
import spoon.SpoonException;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.*;

/*********************************
 Created by Fabian Oraze on 27.11.20
 *********************************/

public class SpoonModel {

    private DPUpdaterBase base;
    private Launcher launcher;
    private CtModel ctModel;
    private String pathM2;
    private List<String> classNames;
    private Map<String, Boolean> jarPaths;
    private static SpoonModel spoonModel;

    public static SpoonModel getSpoonModel(String pathToRepo) {
        if (spoonModel == null) spoonModel = new SpoonModel(pathToRepo);
        return spoonModel;
    }

    private SpoonModel(String pathToProject) {
        setPathM2();
        this.launcher = new MavenLauncher(pathToProject, MavenLauncher.SOURCE_TYPE.ALL_SOURCE);
        this.launcher.getEnvironment().setAutoImports(true);
        this.ctModel = this.launcher.buildModel();
        this.classNames = new ArrayList<>();
        this.jarPaths = new HashMap<>();
        initClassNames();

        this.base = new ImplSpoon(pathToProject);
        computeJarPaths();
    }

    private void initClassNames() {
        this.classNames.clear();
        for (CtType<?> c : this.ctModel.getAllTypes()) this.classNames.add(c.getSimpleName());
    }

    private void setPathM2() {
        String user = System.getProperty("user.name");
        if (System.getProperty("os.name").startsWith("Mac")) {
            this.pathM2 = "/Users/" + user + "/.m2/repository/";
        } else if (System.getProperty("os.name").startsWith("Windows")) {
            this.pathM2 = "C:\\Users\\" + user + "\\.m2\\repository\\";
        } else {
            this.pathM2 = "/home/" + user + "/.m2/repository/";
        }
    }

    public boolean setJarLauncher(String jarPath) {
        try {
            this.launcher = new JarLauncher(jarPath);
            this.ctModel = this.launcher.buildModel();
            this.base = new ImplSpoon(jarPath);
            initClassNames();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Launcher could not be build");
            return false;
        }
    }


    public Map<String, Boolean> computeJarPaths() {
        this.jarPaths.clear();
        try {
            for (Dependency dp : this.base.getPomModel().getDependencies().getDependency()) {
                String postFixJar = dp.getArtifactId() + "-" + dp.getVersion() + ".jar";
                if (System.getProperty("os.name").startsWith("Windows")) {
                    jarPaths.put(this.pathM2 + (dp.getGroupId() + "." + dp.getArtifactId()).replace('.', '\\') + "\\" + dp.getVersion() + "\\" + postFixJar, false);
                } else {
                    jarPaths.put(this.pathM2 + (dp.getGroupId() + "." + dp.getArtifactId()).replace('.', '/') + "/" + dp.getVersion() + "/" + postFixJar, false);
                }
            }
            return this.jarPaths;
        } catch (NullPointerException e) {
            System.out.println("No Dependencies found for given project");
            return null;
        }
    }

    //TODO: only trace invoked methods
    public List<List<String>> iterateMethods() {
        HashMap<String, Set<String>> nonLocalClasses = new HashMap<String, Set<String>>();
        for (CtType<?> s : this.ctModel.getAllTypes()) {
            for (CtMethod<?> m : s.getAllMethods()) {
                searchInvocation(m, nonLocalClasses);
            }
        }
        List<List<String>> classesWithMethods = new ArrayList<>();
        for (String cls : nonLocalClasses.keySet()) {
            List<String> list = new ArrayList<>();
            list.add(cls);
            for (String m : nonLocalClasses.get(cls)) {
                list.add(m);
            }
            classesWithMethods.add(list);
        }
        return classesWithMethods;
    }

    private void searchInvocation(CtMethod method, HashMap set) {
        List<CtInvocation> elements = method.getElements(new TypeFilter<>(CtInvocation.class));
        //if (elements.size() != 0) System.out.println("method: " + method.getSignature());
        for (CtInvocation element : elements) {
            CtTypeReference declaringType = element.getExecutable().getDeclaringType();
            /*try {
                System.out.println("\tinvocation: " +   element.getExecutable() + "\n\t\tqualified name: " + "\033[0;32m" + declaringType.getQualifiedName() + "\033[0m");
            } catch (NullPointerException e) {
                System.err.println("\t---> no qualified name for: " + element + e);
            }*/
            if (declaringType != null && checkJDKClasses(declaringType.getQualifiedName()) && !this.classNames.contains(declaringType.getSimpleName())) {
                if (!set.containsKey(declaringType.getQualifiedName()))
                    set.put(declaringType.getQualifiedName(), new HashSet<String>());
                Set<String> s = (Set<String>) set.get(declaringType.getQualifiedName());
                s.add(element.getExecutable().toString());
            }
            /*try {
                Class<?> cls = Class.forName(declaringType.getQualifiedName());
                System.out.println("\033[0;32m\t\t\tjar path: " + new File(cls.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath() + "\033[0m");
            } catch (ClassNotFoundException e) {
                System.err.println("\t---> class not found for: " + element);
            } catch (URISyntaxException e) {
                System.err.println("\t---> no uri for that element");
            } catch (NullPointerException e) {
                System.err.println("\t---> class path not found for: " + element);
            }*/
        }
    }

    private boolean checkJDKClasses(String qualifiedName) {
        String[] strings = qualifiedName.split("[.]");
        if (strings.length == 1) return true;
        else return !strings[0].equals("java");
    }


}
