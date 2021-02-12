package dp.resolver.tree.model;

/*********************************
 Created by Fabian Oraze on 12.02.21
 *********************************/

public class ModelFactoryImpl implements ModelFactory {

    @Override
    public CallModel createCallModelFromMaven(String pathToMaven) throws Exception {
        return new MavenSpoonModel(pathToMaven);
    }

    @Override
    public CallModel createCallModelFromJar(String pathToJar) throws Exception {
        return new JarSpoonModel(pathToJar);
    }
}
