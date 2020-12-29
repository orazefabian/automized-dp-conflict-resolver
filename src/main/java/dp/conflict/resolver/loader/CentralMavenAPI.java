package dp.conflict.resolver.loader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/*********************************
 Created by Fabian Oraze on 29.12.20
 *********************************/

public class CentralMavenAPI {

    public static final String MAVEN_REPO_URL = "https://repo1.maven.org/maven2";

    /**
     * function which creates the missing folder structure for a non existent jar and
     * then downloads missing jars and poms from central maven repo
     *
     * @param currProjectPath path from jar, is then appended with the correct prefix
     */
    public static synchronized void downloadMissingFiles(String currProjectPath) {
        String[] dirNames = currProjectPath.split("/");
        StringBuilder dirNameNew = new StringBuilder();
        for (int i = 0; i < dirNames.length - 1; i++) {
            dirNameNew.append(dirNames[i]).append("/");
        }
        File dirFile = new File(dirNameNew.toString());
        dirFile.mkdirs();
        System.out.println("Downloading jar and pom from central repo...");
        downloadJar(currProjectPath);
        downloadPom(currProjectPath);

    }

    /**
     * helper function to download and save a jar from central maven rep0
     *
     * @param currProjectPath String path to current project with .jar ending
     */
    private static void downloadJar(String currProjectPath) {
        String url = MAVEN_REPO_URL + currProjectPath.split("/repository")[1];
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOS = new FileOutputStream(currProjectPath)) {
            byte[] data = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
            System.out.println("Downloading Jar finished");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * helper function to download and save a pom file from central maven repo
     *
     * @param currProjectPath String path to current project with .jar ending
     */
    private static void downloadPom(String currProjectPath) {
        String url = MAVEN_REPO_URL + currProjectPath.split("/repository")[1];
        url.replace(".jar", ".pom");
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOS = new FileOutputStream(currProjectPath.replace(".jar", ".pom"))) {
            byte[] data = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
            System.out.println("Downloading Pom finished");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
