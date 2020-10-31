import org.paukov.combinatorics3.Generator;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*********************************
 Created by Fabian Oraze on 22.10.20
 *********************************
 *
 * Implementation of the DPUpdater interface
 * Naive approach which just gets the latest versions of a provided List with the groupIDs of a number of dependencies
 *
 */
public class ImplNaive extends DPUpdaterBase {

    private List<Object> configurations;
    private List<Object> workingConfigurations;
    private int maxConfigurations;

    public ImplNaive(String pathToRepo, int maxConfigurations) throws IOException {
        super(pathToRepo);
        this.configurations = new ArrayList<>();
        this.maxConfigurations = maxConfigurations;
        this.workingConfigurations = new ArrayList<>();
    }

    @Override
    public void computeVersionConfiguration() {
        try {
            saveDependencies();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Generator.cartesianProduct(fullDPsWithVersions.toArray(new ArrayList[fullDPsWithVersions.size()]))
                .stream().forEach(x -> this.configurations.add(x));
        buildConfiguration();

    }

    private void buildConfiguration() {
        if (this.maxConfigurations > configurations.size()) this.maxConfigurations = configurations.size();
        for (int j = this.configurations.size() - 1; j >= this.configurations.size() - maxConfigurations; j--) {
            for (int i = 0; i < getPomModel().getDependencies().getDependency().size(); i++) {
                List<String> tempConf = (List<String>) configurations.get(j);
                getPomModel().getDependencies().getDependency().get(i).setVersion(tempConf.get(i));
            }
            try {
                System.out.println(pomModel);
                writePom(new File(path + "pom.xml"), pomModel);
                boolean buildSuccess = getBuildSuccess(false);
                if (buildSuccess) workingConfigurations.add(configurations.get(j));
                System.out.println(buildSuccess);

            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
    }
}
