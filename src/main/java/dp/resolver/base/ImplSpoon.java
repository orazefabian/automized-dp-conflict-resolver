package dp.resolver.base;

import org.apache.maven.pom._4_0.Model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
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
        System.out.println("not saving dependencies from central maven repo");
    }

    @Override
    public void updateDependencies() {

    }

    @Override
    public List<Object> getWorkingConfigurations() {
        return null;
    }
}
