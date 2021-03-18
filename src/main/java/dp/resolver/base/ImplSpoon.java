package dp.resolver.base;

import dp.resolver.parse.entity.MessagingJar;
import org.apache.maven.pom._4_0.Dependency;
import org.apache.maven.pom._4_0.Model;
import spoon.Launcher;
import spoon.MavenLauncher;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*********************************
 Created by Fabian Oraze on 28.11.20
 *********************************/

public class ImplSpoon extends DPUpdaterBase {


    /**
     * @param pathToRepo String pointing to the root repo directory
     */
    public ImplSpoon(String pathToRepo) {
        super(pathToRepo);
        try {
            updateDependencies();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * adapted createPomModel method to create model from a effective pom
     *
     * @param effectivePomFile the file from which the model object should be created
     * @return the pom model object of the effective pom
     */
    public Model createEffectivePomModel(File effectivePomFile) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Model.class);

            Unmarshaller unmarshaller = jc.createUnmarshaller();
            JAXBElement<Model> feed = unmarshaller.unmarshal(new StreamSource(new FileInputStream(effectivePomFile)), Model.class);

            return feed.getValue();
        } catch (Exception e) { //currently we do nothing!
            e.printStackTrace();
        }
        return null;
    }

    public String getPath() {
        return this.path;
    }

    @Override
    public void saveDependencies() {
    }

    @Override
    public void updateDependencies() {
    }

    @Override
    public List<Object> getWorkingConfigurations() {
        return null;
    }

    /**
     * creates effective pom via the ImplSpoon object and retrieves the version from it
     *
     * @param launcher
     * @return the version from the effective pom
     * @throws JAXBException        if marshalling fails
     * @throws IOException          if reading or writing file fails
     * @throws InterruptedException if process gets interrupted
     */
    public Model getEffectivePomModel(Launcher launcher) throws JAXBException, IOException, InterruptedException {
        File currPro = new File(getPath());
        String currPath;
        boolean fromMaven;
        if (launcher instanceof MavenLauncher) {
            currPath = currPro.getAbsolutePath();
            fromMaven = true;
        } else {
            currPath = currPro.getAbsolutePath().substring(0, currPro.getAbsolutePath().lastIndexOf(File.separator));
            fromMaven = false;
        }
        File pom = new File(currPath + File.separator + "pom.xml");
        if (!pom.exists()) {
            // must write pom.xml file before creating effective pom, because it does not recognize .pom endings
            writePom(new File(currPath + File.separator + "pom.xml"), getPomModel());
        }
        File effectivePom = createEffectivePom(currPro, fromMaven);
        Model pomModel = createEffectivePomModel(effectivePom);

        return pomModel;
    }


    /**
     * write a new pom for each answer from clingo
     *
     * @param answers the solution from clingo as a List of configurations
     */
    public void updateAndWritePomModels(List<List<String>> answers, int limit) {
        int configCount = 0;
        for (List<String> configuration : answers) {
            List<Dependency> dpsNew = new ArrayList<>();
            configCount++;
            if (configCount > limit) break;
            for (String jar : configuration) {
                MessagingJar dependency = new MessagingJar();
                dependency.fillJarFromFullPath(jar);
                appendDps(dependency, dpsNew);
            }
            createNewPomConfig(dpsNew, configCount);
        }
    }

    private void createNewPomConfig(List<Dependency> dependencies, int suffix) {
        this.pomModel.getDependencies().getDependency().clear();
        for (Dependency dp : dependencies) {
            this.pomModel.getDependencies().getDependency().add(dp);
        }
        try {
            writePom(new File("pom-config-" + suffix + ".xml"), this.pomModel);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private void appendDps(MessagingJar dependency, List<Dependency> dpsNew) {
        Dependency dp = new Dependency();
        dp.setGroupId(dependency.getGroupId());
        dp.setArtifactId(dependency.getArtifactId());
        dp.setVersion(dependency.getVersion());
        dpsNew.add(dp);
    }

}
