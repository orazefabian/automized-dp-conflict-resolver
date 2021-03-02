package dp.resolver.tree.model;

import dp.resolver.tree.element.Invocation;

import java.util.List;

/*********************************
 Created by Fabian Oraze on 12.02.21
 *********************************/

public interface ModelFactory {

    /**
     * creates an instance of a CallModel from a maven project
     *
     * @param pathToMaven the full path to the root of the local maven project
     * @return {@link CallModel}
     */
    CallModel createCallModelFromMaven(String pathToMaven, List<Invocation> leafInvocations) throws Exception;


    /**
     * creates an instance of a CallModel from a jar
     *
     * @param pathToJar the full path to the local jar ending with .jar
     * @return {@link CallModel}
     */
    CallModel createCallModelFromJar(String pathToJar, List<Invocation> leafInvocations) throws Exception;


}
