package dp;

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

    }

    @Override
    public void saveDependencies(){
        System.out.println("Not accessing maven repo");
    }

    @Override
    public void updateDependencies() {

    }

    @Override
    public List<Object> getWorkingConfigurations() {
        return null;
    }
}
