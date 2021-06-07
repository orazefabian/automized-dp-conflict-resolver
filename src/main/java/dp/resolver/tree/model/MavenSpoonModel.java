package dp.resolver.tree.model;

import dp.resolver.base.ImplSpoon;
import dp.resolver.tree.element.Invocation;
import spoon.MavenLauncher;

import java.io.*;
import java.util.List;
import java.util.Set;

/*********************************
 Created by Fabian Oraze on 27.11.20
 *********************************/

public class MavenSpoonModel extends CallModel {


    /**
     * object which builds a new spoon launcher which provides a AST
     *
     * @param pathToProject String to a project, can be maven root folder or path to .jar file
     * @param leafInvocations
     * @param isRoot
     * @throws Exception if building the spoon model fails
     */
    protected MavenSpoonModel(String pathToProject, Set<Invocation> leafInvocations, boolean isRoot) throws Exception {
        super(pathToProject, leafInvocations, isRoot);
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

        this.launcher = new MavenLauncher(this.currProjectPath, MavenLauncher.SOURCE_TYPE.ALL_SOURCE); // change source type to all_source to include tests
        this.baseModel = new ImplSpoon(this.currProjectPath);
        searchModulesForPom(new File(currProjectPath));
        this.ctModel = this.launcher.buildModel();

    }
}
