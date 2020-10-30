import org.apache.maven.pom._4_0.Model;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

/*********************************
 * Created by Fabian Oraze on 22.10.20
 *********************************
 *
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
     * @throws IOException when creating the file given a path fails or reading is not possible
     */
    Model createPomModel(String repoPath) throws IOException;

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
     * print the dependencies of the current model object
     * @throws Exception when reading from model object fails
     */
    void getDependencies() throws Exception;



    DPConfiguration computeVersionConfiguration();
}