package dp.resolver.tree.model;

import dp.resolver.tree.element.Invocation;

import java.util.List;

/*********************************
 Created by Fabian Oraze on 12.02.21
 *********************************/

public class ModelFactoryImpl implements ModelFactory {

    @Override
    public CallModel createCallModelFromMaven(String pathToMaven, List<Invocation> leafInvocations) throws Exception {
        return new MavenSpoonModel(pathToMaven, leafInvocations);
    }

    @Override
    public CallModel createCallModelFromJar(String pathToJar, List<Invocation> leafInvocations) throws Exception {
        return new JarSpoonModel(pathToJar, leafInvocations);
    }
}
