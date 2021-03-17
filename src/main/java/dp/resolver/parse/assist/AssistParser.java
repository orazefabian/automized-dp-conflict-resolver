package dp.resolver.parse.assist;

import dp.resolver.parse.entity.MessagingClazz;
import dp.resolver.parse.entity.MessagingJar;

import java.io.File;
import java.io.IOException;
import java.util.List;

/*********************************
 Created by Fabian Oraze on 29.01.21
 *********************************/

public class AssistParser {

    /**
     * parses a jar file to {@link MessagingClazz} objects
     *
     * @param path the full path to the Jar
     * @return a list with all classes from jar
     */
    public static List<MessagingClazz> getJarClassList(String path) {

        MessagingJar messagingJar = new MessagingJar();
        messagingJar.fillJarFromFullPath(path);
        ClazzExtractionWorker extractionWorker = new ClazzExtractionWorker(messagingJar);
        try {
            extractionWorker.analyzeJarWithBCEL(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messagingJar.getClazzes();
    }

}
