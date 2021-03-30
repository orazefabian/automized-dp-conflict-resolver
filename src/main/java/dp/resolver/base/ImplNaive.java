package dp.resolver.base;
import org.paukov.combinatorics3.Generator;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*********************************
 Created by Fabian Oraze on 22.10.20
 *********************************
 *
 * Implementation of the dp.DPUpdater interface
 * Naive approach which just gets the latest versions of a provided List with the groupIDs of a number of dependencies
 *
 */
public class ImplNaive extends DPUpdaterBase {

    /**
     * final distance measure list, specifies how deep a single configuration can be switched back
     */
    protected final List<Integer> DISTANCE_MEASURE = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5));
    private List<Object> configurations;
    private List<Object> workingConfigurations;
    private int maxDistanceDepth;
    private boolean stopAtFirstWorkingConf;

    /**
     * class configuration, if true each build output will be printed to console, default false
     */
    static boolean PRINT_OUTPUT = false;

    /**
     * Default constructor, generates impl of a given repo and sets maxConfigurations to 0 which
     * means it the algorithm will only find and save the first working version configuration
     *
     * @param pathToRepo String with the absolute path to the repo
     */
    public ImplNaive(String pathToRepo) {
        super(pathToRepo);
        this.configurations = new ArrayList<>();
        this.maxDistanceDepth = 0;
        this.workingConfigurations = new ArrayList<>();
        this.stopAtFirstWorkingConf = true;
    }

    /**
     * overloaded constructor with additional parameter for depth measure
     *
     * @param pathToRepo String with the absolute path to the repo
     * @param maxDepth   sets the depth limit when the search for additional version configurations should stop
     */
    public ImplNaive(String pathToRepo, int maxDepth) {
        super(pathToRepo);
        this.configurations = new ArrayList<>();
        if (maxDepth < 0) this.maxDistanceDepth = 0;
        else this.maxDistanceDepth = maxDepth;
        this.workingConfigurations = new ArrayList<>();
        this.stopAtFirstWorkingConf = true;
    }

    /**
     * overloaded constructor with additional two parameters fo depth measure and stopping configuration
     *
     * @param pathToRepo             String with the absolute path to the repo
     * @param maxDepth               sets the depth limit when the search for additional version configurations should stop
     * @param stopAtFirstWorkingConf boolean value that specifies if algorithm should stop at the firs working build or check all configurations for a given depth
     */
    public ImplNaive(String pathToRepo, int maxDepth, boolean stopAtFirstWorkingConf) {
        super(pathToRepo);
        this.configurations = new ArrayList<>();
        if (maxDepth < 0) this.maxDistanceDepth = 0;
        else this.maxDistanceDepth = maxDepth;
        this.workingConfigurations = new ArrayList<>();
        this.stopAtFirstWorkingConf = stopAtFirstWorkingConf;
    }

    /**
     * the core method of the implementation used to get the dependency-versions and update them in pom
     * creates a {@link Generator} and computes the permutations of all versions of the dependencies used in the pom with respect to
     * the DISTANCE_MEASURE and adds the computed products to the configuration list
     * afterwards calls the buildConfiguration method which tries to build one configuration after another
     */
    @Override
    public void updateDependencies() {
        for (int i = 0; i <= maxDistanceDepth; i++) {
            int finalI = i;
            Generator.permutation(DISTANCE_MEASURE).withRepetitions(dpVersionList.size()).stream().forEach(x -> {
                if (checkDistance(finalI, x)) {
                    List<String> curr = new ArrayList<>();
                    for (int j = 0; j < x.size(); j++) {
                        int len = dpVersionList.get(j).size() - 1;
                        try {
                            curr.add(dpVersionList.get(j).get(len - x.get(j)));
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("Max depth reached for dp at index: " + j);
                            curr.add(dpVersionList.get(j).get(0));
                        }
                    }
                    configurations.add(curr);
                }
            });
        }
        buildConfiguration();
    }

    /**
     * private helper function to calculate the distance measure for a list of versions
     *
     * @param maxDist the depth distance that is currently searched for
     * @param list    Integers referring to the version lists, each number representing how many versions the dp at that index is turned back
     * @return weather the current list has the specified depth distance
     */
    private boolean checkDistance(int maxDist, List<Integer> list) {
        int currDist = 0;
        for (Integer num : list) {
            currDist += num;
        }
        return (currDist == maxDist);
    }

    /**
     * loops over the configurations and sets the dependencies of the model for each time, then calls buildPom
     */
    private void buildConfiguration() {
        for (int j = 0; j < this.configurations.size(); j++) {
            for (int i = 0; i < getPomModel().getDependencies().getDependency().size(); i++) {
                List<String> tempConf = (List<String>) configurations.get(j);
                getPomModel().getDependencies().getDependency().get(i).setVersion(tempConf.get(i));
            }
            try {
                boolean success = buildPom(j);
                if (success && this.stopAtFirstWorkingConf) break;
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * tries to build current version of the pom model by deserializing it back to a xml file and then running the mvn command
     *
     * @param index the current index of the configurations that was used in this build
     * @return boolean whether the build was successful
     * @throws JAXBException when marshalling fails
     */
    private boolean buildPom(int index) throws JAXBException {
        writePom(new File(path + "pom.xml"), pomModel);
        boolean buildSuccess = getBuildSuccess(PRINT_OUTPUT);
        if (buildSuccess) workingConfigurations.add(configurations.get(index));
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
