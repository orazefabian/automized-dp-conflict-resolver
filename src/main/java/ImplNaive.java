import org.paukov.combinatorics3.Generator;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.ArrayList;
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
    private List<String> refVersion;

    /**
     * Default constructor, generates impl of a given repo and sets maxConfigurations to -1 which
     * means it the algorithm will only find and save the first working version configuration
     * @param pathToRepo String with the absolute path to the repo
     */
    public ImplNaive(String pathToRepo)  {
        super(pathToRepo);
        this.configurations = new ArrayList<>();
        this.maxConfigurations = -1;
        this.workingConfigurations = new ArrayList<>();
    }

    /**
     * special constructor with additional parameter
     * @param pathToRepo String with the absolute path to the repo
     * @param maxConfigurations sets the variable to a limit when the search for additional version configurations should stop
     */
    public ImplNaive(String pathToRepo, int maxConfigurations)  {
        super(pathToRepo);
        this.configurations = new ArrayList<>();
        if (maxConfigurations < -1) this.maxConfigurations = -1;
        else this.maxConfigurations = maxConfigurations;
        this.workingConfigurations = new ArrayList<>();
    }


    /**
     * the core method of the implementation used to get the dependency-versions and update them in pom
     * creates a {@link Generator} and computes the cartesian product of all versions of the dependencies used in the pom
     * adds the computed products to the configuration list and calls the buildConfigurations method which tries to build one configuration
     * after another, all working configurations will be stored in a class variable
     */
    @Override
    public void updateDependencies() {
        try {
            saveDependencies();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Generator.cartesianProduct(dpVersionList.toArray(new ArrayList[dpVersionList.size()]))
                .stream().forEach(x -> this.configurations.add(x));
        this.refVersion = (List<String>) configurations.get(configurations.size()-1);
        buildConfiguration();

    }

    /**
     * loops over the configurations and sets the dependencies of the model for each time, then calls buildPom
     */
    private void buildConfiguration() {
        boolean findFistWorkingConfig = false;
        if (this.maxConfigurations == -1) findFistWorkingConfig = true;
        if (this.maxConfigurations > configurations.size() || this.maxConfigurations == -1)
            this.maxConfigurations = configurations.size();
        // TODO: travers the configs with a distance measure
        for (int j = this.configurations.size() - 1; j >= this.configurations.size() - maxConfigurations; j--) {
            for (int i = 0; i < getPomModel().getDependencies().getDependency().size(); i++) {
                List<String> tempConf = (List<String>) configurations.get(j);
                getPomModel().getDependencies().getDependency().get(i).setVersion(tempConf.get(i));
            }
            try {
                boolean success = buildPom(j);
                if (success && findFistWorkingConfig) break;
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * tries to build current version of the pom model by deserializing it back to a xml file and then running the mvn command
     * @param index the current index of the configurations that was used in this build
     * @return boolean whether the build was successful
     * @throws JAXBException when marshalling fails
     */
    private boolean buildPom(int index) throws JAXBException {
        writePom(new File(path + "pom.xml"), pomModel);
        boolean buildSuccess = getBuildSuccess(true);
        if (buildSuccess) workingConfigurations.add(configurations.get(index));
        System.out.println(buildSuccess);
        return buildSuccess;
    }

    /**
     * @return a list with all working configurations for the used dependencies, if updateDependencies was not called before
     * it will return an empty List, if updateDependencies did not find any working dependencies it will also return a empty list
     */
    @Override
    public List<Object> getWorkingConfigurations() {
        return this.workingConfigurations;
    }
}
