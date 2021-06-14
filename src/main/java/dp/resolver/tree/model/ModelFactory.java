package dp.resolver.tree.model;

import dp.resolver.tree.CallTree;

/*********************************
 Created by Fabian Oraze on 12.02.21
 *********************************/

public interface ModelFactory {

    /**
     * creates an instance of a CallModel from a maven project
     *
     * @param pathToMaven the full path to the root of the local maven project
     * @param callTree
     * @return {@link CallModel}
     */
    CallModel createCallModelFromMaven(String pathToMaven, CallTree callTree) throws Exception;


    /**
     * creates an instance of a CallModel from a jar
     *
     * @param pathToJar the full path to the local jar ending with .jar
     * @param callTree
     * @return {@link CallModel}
     */
    CallModel createCallModelFromJar(String pathToJar, CallTree callTree) throws Exception;


    /**
     * create a root instance of a CallModel from a jar
     * @param pathToJar the full path to the local jar ending with .jar
     * @param callTree
     * @return {@link CallModel}
     */
    CallModel createRootCallModelFromJar(String pathToJar, CallTree callTree) throws Exception;


    /**
     * create a root instance of a CallModel from a maven project
     * @param pathToMaven the full path to the root of the local maven projectr
     * @param callTree
     * @return {@link CallModel}
     */
    CallModel createRootCallModelFromMaven(String pathToMaven, CallTree callTree) throws Exception;
}
