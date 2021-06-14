package dp.resolver.tree.model;

import dp.resolver.tree.CallTree;

/*********************************
 Created by Fabian Oraze on 12.02.21
 *********************************/

public class ModelFactoryImpl implements ModelFactory {

    @Override
    public CallModel createCallModelFromMaven(String pathToMaven, CallTree callTree) throws Exception {
        return new MavenSpoonModel(pathToMaven, callTree, false);
    }

    @Override
    public CallModel createCallModelFromJar(String pathToJar, CallTree callTree) throws Exception {
        return new JarSpoonModel(pathToJar, callTree, false);
    }

    @Override
    public CallModel createRootCallModelFromJar(String pathToJar, CallTree callTree) throws Exception {
        return new JarSpoonModel(pathToJar, callTree, true);
    }

    @Override
    public CallModel createRootCallModelFromMaven(String pathToMaven, CallTree callTree) throws Exception{
        return new MavenSpoonModel(pathToMaven, callTree, true);

    }
}
