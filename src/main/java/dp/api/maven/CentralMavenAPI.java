package dp.api.maven;

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
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/*********************************
 Created by Fabian Oraze on 29.12.20
 *********************************/

public class CentralMavenAPI {

    private static final String MAVEN_REPO_URL = "https://repo1.maven.org/maven2";
    private static String pathM2;
    private static int MAX_VERSIONS_NUM_FROM_CMR = 10;

    /**
     * sets the maximal number of versions that should be downloaded when accessing all version from central repo (default is 10)
     *
     * @param maxVersionsNum number of max newest versions e.g. 'maxVersionsNum = 2' means the two newest versions are downloaded
     */
    public static void setMaxVersionsNumFromCmr(int maxVersionsNum) {
        MAX_VERSIONS_NUM_FROM_CMR = maxVersionsNum;
    }

    /**
     * function which creates the missing folder structure for a non existent jar and
     * then downloads missing jars and poms from central maven repo
     *
     * @param currProjectPath path from jar, is then appended with the correct prefix
     */
    public static synchronized void downloadMissingFiles(String currProjectPath) {
        // check if the folder structure is given
        createFolderStructure(currProjectPath);
        System.out.println("Downloading jar and pom from central repo...");
        downloadJar(currProjectPath);
        downloadPom(currProjectPath);

    }

    /**
     * creates folder structure for a given path if it is not present
     *
     * @param currProjectPath mostly path to a jar
     */
    private static void createFolderStructure(String currProjectPath) {
        if (!Files.exists(Path.of(currProjectPath))) {
            String[] dirNames = currProjectPath.split("/");
            StringBuilder dirNameNew = new StringBuilder();
            for (int i = 0; i < dirNames.length - 1; i++) {
                dirNameNew.append(dirNames[i]).append("/");
            }
            File dirFile = new File(dirNameNew.toString());
            dirFile.mkdirs();
        }
    }

    /**
     * function that retrieves all versions for a given jar and downloads them
     *
     * @param path       the path to the local jar file
     * @param groupID    the group id of the dependency
     * @param artifactID the artifact id of the dependency
     * @throws IOException                  when reading or writing file fails
     * @throws ParserConfigurationException when creating documentBuilder fails
     * @throws SAXException                 when parsing dom object fails
     */
    public static void getAllVersionsFromCMR(String groupID, String artifactID, String path) throws IOException, ParserConfigurationException, SAXException {
        String suffix = path.split("repository" + File.separator)[1];
        String infix = suffix.substring(0, suffix.lastIndexOf(File.separator));
        infix = infix.substring(0, infix.lastIndexOf(File.separator));
        URL url = new URL(MAVEN_REPO_URL + "/" + infix + "/maven-metadata.xml");

        //URL url = new URL(MAVEN_REPO_URL + File.separator + groupID + File.separator + artifactID + "/maven-metadata.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(url.openStream());
        NodeList nodes = doc.getElementsByTagName("version");
        setPathM2();
        for (int i = nodes.getLength() - 1; i >= 0; i--) {
            String version = nodes.item(i).getTextContent();
            /*String jarPath = pathM2 + groupID + File.separator + artifactID + File.separator + version
                    + File.separator + artifactID + "-" + version + ".jar";*/
            String jarPath = pathM2 + infix + File.separator + version
                    + File.separator + artifactID + "-" + version + ".jar";
            downloadMissingFiles(jarPath);
            if (nodes.getLength() - i >= MAX_VERSIONS_NUM_FROM_CMR) break; // only the newest versions until the max limit
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
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fileOS = new FileOutputStream(file, false);
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
        url = url.replace(".jar", ".pom");
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
            File file = new File(path.replace(".jar", ".pom"));
            file.createNewFile();
            FileOutputStream fileOS = new FileOutputStream(file, false);
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
