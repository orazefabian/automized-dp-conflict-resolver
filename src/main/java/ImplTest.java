import java.util.ArrayList;
import java.util.List;

/*********************************
 Created by Fabian Oraze on 22.10.20
 *********************************/

public class ImplTest implements DependencyUpdater {


    public List<String> getURIs(List<String> dps) {
        List<String> finals = new ArrayList<String>();
        for (int i = 0; i < dps.size(); i++) {
            String dp = dps.get(i);
            finals.add(DependencyUpdater.url + dp + getPostfix(dp));
        }
        return finals;

    }

    private String getPostfix(String url) {
        String[] subStr = url.split("/");
        int len = subStr.length;
        String postfix = "/" + subStr[len - 2] + "-" + subStr[len - 1];
        return postfix;
    }

}
