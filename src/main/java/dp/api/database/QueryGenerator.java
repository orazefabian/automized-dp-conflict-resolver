package dp.api.database;

import java.util.List;

/*********************************
 Created by Fabian Oraze on 12.02.21
 *********************************/

public interface QueryGenerator {

    String getJar(String jar);

    List<String> getClassesFromJar(String jar);

    List<String> getMethodsFromClass(String jar, String nameClass);


}
