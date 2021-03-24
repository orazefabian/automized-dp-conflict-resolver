package dp.resolver.tree.model;

import dp.resolver.tree.element.Invocation;

import java.util.List;
import java.util.Set;

/*********************************
 Created by Fabian Oraze on 12.02.21
 *********************************/

public class ModelFactoryImpl implements ModelFactory {

    @Override
    public CallModel createCallModelFromMaven(String pathToMaven, Set<Invocation> leafInvocations) throws Exception {
        return new MavenSpoonModel(pathToMaven, leafInvocations);
    }

    @Override
    public CallModel createCallModelFromJar(String pathToJar, Set<Invocation> leafInvocations) throws Exception {
        return new JarSpoonModel(pathToJar, leafInvocations);
    }
}
