package dp.resolver.tree.model;

import dp.api.maven.CentralMavenAPI;
import dp.resolver.base.ImplSpoon;
import dp.resolver.tree.element.Invocation;
import spoon.JarLauncher;
import java.io.File;
import java.util.List;
import java.util.Set;

/*********************************
 Created by Fabian Oraze on 12.02.21
 *********************************/

public class JarSpoonModel extends CallModel {


    /**
     * object which builds a new spoon launcher which provides a AST
     *
     * @param pathToProject String to a project, can be maven root folder or path to .jar file
     * @throws Exception if building the spoon model fails
     */
    protected JarSpoonModel(String pathToProject, Set<Invocation> leafInvocations) throws Exception {
        super(pathToProject, leafInvocations);
        System.out.println("Starting to build Maven spoon model from " + pathToProject + "...");
        initLauncherAndCreatePomModels();
        System.out.println("Building spoon model finished");
        initClassNames();
        try {
            getDependenciesToJarPaths();
        } catch (NullPointerException e) {
            System.err.println("No dependencies for project: " + pathToProject);
        }
    }


    /**
     * function which initializes a new spoon launcher and fills pomModel list with all poms located in Maven-project/jar
     */
    protected void initLauncherAndCreatePomModels() {
        File jar = new File(this.currProjectPath);
        File pom = new File(this.currProjectPath.replace(".jar", ".pom"));
        if (!jar.exists() || !pom.exists()) {
            System.out.println("Jar and/or pom not found... proceeding with download");
            CentralMavenAPI.downloadMissingFiles(this.currProjectPath);
        }
        this.launcher = new JarLauncher(this.currProjectPath);
        // add new pom model
        this.baseModel = new ImplSpoon(this.currProjectPath);
        this.ctModel = this.launcher.buildModel();

    }
}
