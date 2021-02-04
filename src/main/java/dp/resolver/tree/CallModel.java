package dp.resolver.tree;

import dp.resolver.tree.element.CallNode;
import dp.resolver.tree.element.Invocation;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/*********************************
 Created by Fabian Oraze on 03.02.21
 *********************************/

public interface CallModel {


    String getCurrProjectPath();

    /**
     * @return list of CallNodes of the current model
     */
    List<CallNode> getCallNodes();

    /**
     * set the available CallNodes for current model
     *
     * @param callNodes list of {@link CallNode}
     */
    void setCallNodes(List<CallNode> callNodes);

    /**
     * compute jar paths for all dependencies of the current spoon model
     * further checks if effective poms are needed to compute the correct jar file
     *
     * @return HashMap with String pathsToJar as keys and a initial boolean value false
     */
    Map<String, Boolean> computeJarPaths() throws NullPointerException, IOException, InterruptedException, JAXBException;

    /**
     * function that iterates over all methods of all classes of the current spoon model and analyzes the invocations of
     * used methods
     *
     * @param leafInvocations a list of current leafInvocations that represent the bottom of the current call tree
     * @return list of current used CallNodes
     */
    List<CallNode> analyzeModel(List<Invocation> leafInvocations);
}
