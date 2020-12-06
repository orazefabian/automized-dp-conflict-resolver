package dp.conflict.resolver.base;

import org.apache.maven.pom._4_0.Model;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/*********************************
 * Created by Fabian Oraze on 22.10.20
 *********************************
 *
 * interface for the dependency updater tool, which contains all abstract methods that are necessary for each implementation
 */
public interface DPUpdater {

    /**
     * should try to build the project given a file and an optional cmd command
     * @param repo file referring to pom.xml
     * @param buildOutput where the build process should log the output
     * @param cmd additional command e.d. "maven -U clean package"
     * @throws IOException when error while reading file occurs
     * @throws InterruptedException when build process is interrupted
     */
    void buildProject(File repo, PrintStream buildOutput, String cmd) throws IOException, InterruptedException;

    /**
     * deserialization of a pom file to a java object given the path to a repo
     * @param repoPath String with the absolute path to a maven repo folder
     * @return a {@link Model} object which should represent the pom.xml
     * @throws Exception when Error occurs during process
     * */
    Model createPomModel(String repoPath) throws Exception;

    /**
     * serialization of a java object back to a pom.xml file
     * @param file {@link File} of the pom.xml that should be written to
     * @param model {@link Model} object representation of the pom
     * @throws JAXBException when marshalling {@link javax.xml.bind.Marshaller} fails
     */
    void writePom(File file, Model model) throws JAXBException;

    /**
     * returns the current model of the pom
     * @return {@link Model} model object of pom
     */
    Model getPomModel();

    /**
     * @param printOutput if true then the buildLog of the last build should be shown
     * @return true if last build was successful
     */
    boolean getBuildSuccess(boolean printOutput);

    /**
     * save the dependencies of the current model object
     * @throws Exception when reading from model object fails
     */
    void saveDependencies() throws Exception;


    /**
     * most important method of the interface, handles the different approaches for updating the dependencies and
     * computes different version-configurations based on the implementation
     * the method should always save the successful configurations for later usage
     */
    void updateDependencies() throws Exception;

    /**
     * should be used to get the successful version configurations after updateDependencies method has been run
     * @return List with objects used in a specific implementation which contain the configurations
     */
    List<Object> getWorkingConfigurations();
}