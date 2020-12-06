package dp.conflict.resolver.base;
import java.util.List;

/*********************************
 Created by Fabian Oraze on 28.11.20
 *********************************/

public class ImplSpoon extends DPUpdaterBase {

    private final String repoPath;

    /**
     * @param pathToRepo String pointing to the root repo directory
     */
    public ImplSpoon(String pathToRepo, String localReposPath) {
        super(pathToRepo);
        this.repoPath = localReposPath;
        try {
            updateDependencies();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveDependencies() {
        System.out.println("not saving dependencies from central maven repo");
    }

    @Override
    public void updateDependencies() throws Exception {

    }

    @Override
    public List<Object> getWorkingConfigurations() {
        return null;
    }
}
