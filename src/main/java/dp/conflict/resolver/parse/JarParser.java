package dp.conflict.resolver.parse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*********************************
 Created by Fabian Oraze on 22.12.20
 *********************************/

public class JarParser {

    /**
     * function that parses the content(methods) of a given class in a given jar
     *
     * @param jarPath             the complete path to the jar file
     * @param fullyQualifiedClass the name of the class to parse, should be separated by File.separators e.g. "/"
     * @return the methods of the given class in a String object
     * @throws IOException          if reading file fails
     * @throws InterruptedException if process gets interrupted
     */
    public static String parseJarContent(String jarPath, String fullyQualifiedClass) throws IOException, InterruptedException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream buildOutputStream = new PrintStream(outputStream);

        String[] structure = jarPath.split(File.separator);
        StringBuilder folder = new StringBuilder();
        String jar = structure[structure.length - 1];
        for (int i = 0; i < structure.length - 1; i++) {
            folder.append(structure[i]).append(File.separator);
        }
        ProcessBuilder pb;

        String cmd = "javap -classpath " + jar + " " + fullyQualifiedClass;

        if (System.getProperty("os.name").startsWith("Windows")) {
            pb = new ProcessBuilder("cmd.exe", "/c", "cd " + folder.toString() + " && " + cmd);
        } else {
            pb = new ProcessBuilder("/bin/bash", "-c", "cd " + folder.toString() + " ; " + cmd);
        }

        Process p = pb.start();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream()));

        //String content = "";
        List<String> lines = new ArrayList<>();
        String line = "";
        System.out.println("Processing jar: " + jarPath + "  --> class: " + fullyQualifiedClass + "...");
        while ((line = reader.readLine()) != null) {
            lines.add(line);
            //content = content + line + System.getProperty("line.separator");
            if (buildOutputStream != null) {
                buildOutputStream.println(line);
                //listener //Refactor that only listeners get called here (and make a listener for the print stream
                String finalLine = line;
                // this.repairListeners.forEach(x->x.newBuildLine(finalLine));
            }
        }
        p.waitFor();
        String content = outputStream.toString(StandardCharsets.UTF_8);
        outputStream.flush();
        buildOutputStream.flush();
        return content;
    }

    /**
     * function which parses the contents from a .jar file to a string
     *
     * @param jarPath the complete path to the jar file
     * @return a String containing all declared files (classes) in a jar
     * @throws IOException          if reading file is not possible
     * @throws InterruptedException if the process gets interrupted
     */
    public static String parseJarClasses(String jarPath) throws IOException, InterruptedException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream buildOutputStream = new PrintStream(outputStream);

        String[] structure = jarPath.split(File.separator);
        StringBuilder folder = new StringBuilder();
        String jar = structure[structure.length - 1];
        for (int i = 0; i < structure.length - 1; i++) {
            folder.append(structure[i]).append(File.separator);
        }
        ProcessBuilder pb;

        if (System.getProperty("os.name").startsWith("Windows")) {
            pb = new ProcessBuilder("cmd.exe", "/c", "cd " + folder.toString() + " && jar tf " + jar);
        } else {
            pb = new ProcessBuilder("/bin/bash", "-c", "cd " + folder.toString() + " ; jar tf " + jar);
        }

        Process p = pb.start();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream()));

        //String content = "";
        List<String> lines = new ArrayList<>();
        String line = "";
        System.out.println("Preprocessing jar: " + jarPath + "...");
        while ((line = reader.readLine()) != null) {
            lines.add(line);
            //content = content + line + System.getProperty("line.separator");
            if (buildOutputStream != null) {
                buildOutputStream.println(line);
                //listener //Refactor that only listeners get called here (and make a listener for the print stream
                String finalLine = line;
                // this.repairListeners.forEach(x->x.newBuildLine(finalLine));
            }
        }
        p.waitFor();
        String content = outputStream.toString(StandardCharsets.UTF_8);
        outputStream.flush();
        buildOutputStream.flush();
        return content;
    }

}
