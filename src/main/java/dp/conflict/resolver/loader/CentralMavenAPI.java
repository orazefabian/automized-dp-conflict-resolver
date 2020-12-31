package dp.conflict.resolver.loader;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/*********************************
 Created by Fabian Oraze on 29.12.20
 *********************************/

public class CentralMavenAPI {

    private static final String MAVEN_REPO_URL = "https://repo1.maven.org/maven2";
    private static String pathM2;

    /**
     * function which creates the missing folder structure for a non existent jar and
     * then downloads missing jars and poms from central maven repo
     *
     * @param currProjectPath path from jar, is then appended with the correct prefix
     */
    public static synchronized void downloadMissingFiles(String currProjectPath) {
        // check if the folder structure is given
        if (Files.exists(Path.of(currProjectPath))) {
            String[] dirNames = currProjectPath.split("/");
            StringBuilder dirNameNew = new StringBuilder();
            for (int i = 0; i < dirNames.length - 1; i++) {
                dirNameNew.append(dirNames[i]).append("/");
            }
            File dirFile = new File(dirNameNew.toString());
            dirFile.mkdirs();
        }
        System.out.println("Downloading jar and pom from central repo...");
        downloadJar(currProjectPath);
        downloadPom(currProjectPath);

    }

    /**
     * function that retrieves all versions for a given jar and downloads them
     *
     * @param groupID    the group id of the dependency
     * @param artifactID the artifact id of the dependency
     * @throws IOException                  when reading or writing file fails
     * @throws ParserConfigurationException when creating documentBuilder fails
     * @throws SAXException                 when parsing dom object fails
     */
    public static void getAllVersionsFromCMR(String groupID, String artifactID) throws IOException, ParserConfigurationException, SAXException {
        URL url = new URL(MAVEN_REPO_URL + File.separator + groupID + File.separator + artifactID + "/maven-metadata.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(url.openStream());
        NodeList nodes = doc.getElementsByTagName("version");
        setPathM2();
        for (int i = 0; i < nodes.getLength(); i++) {
            String version = nodes.item(i).getTextContent();
            String jarPath = pathM2 + groupID + File.separator + artifactID + File.separator + version
                    + File.separator + artifactID + "-" + version + ".jar";
            downloadMissingFiles(jarPath);
        }
    }

    /**
     * creates path to local repository folder where maven dependency jars are saved
     */
    private static void setPathM2() {
        String user = System.getProperty("user.name");
        if (System.getProperty("os.name").startsWith("Mac")) {
            pathM2 = "/Users/" + user + "/.m2/repository/";
        } else if (System.getProperty("os.name").startsWith("Windows")) {
            pathM2 = "C:\\Users\\" + user + "\\.m2\\repository\\";
        } else {
            pathM2 = "/home/" + user + "/.m2/repository/";
        }
    }

    /**
     * helper function to download and save a jar from central maven rep0
     *
     * @param path String path to current project with .jar ending
     */
    private static void downloadJar(String path) {
        String url = MAVEN_REPO_URL + path.split("/repository")[1];
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOS = new FileOutputStream(path)) {
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
     * @param path String path to current project with .jar ending
     */
    private static void downloadPom(String path) {
        String url = MAVEN_REPO_URL + path.split("/repository")[1];
        url.replace(".jar", ".pom");
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOS = new FileOutputStream(path.replace(".jar", ".pom"))) {
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
