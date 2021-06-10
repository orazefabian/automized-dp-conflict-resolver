package dp.resolver.asp;

/*********************************
 Created by Fabian Oraze on 12.01.21
 *********************************/

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * runs clingo command on console with lp files from same package: rules.lp and facts.lp
 */
public class ClingoSolver {
    //OPTIONS TO GIVE ALL OPTIMUM ANSWERS
    private static final String OPTIONS = "--opt-mode=optN --quiet=1,2";
    private static final String CMD = "clingo rules.lp facts.lp " + OPTIONS;
    private static final String PATH = System.getProperty("user.dir")/* + "src/main/java/dp/resolver/asp"*/;


    /**
     * calls clingo command and passes it the rules.lp, facts.lp file and additional options
     *
     * @return the content of the process output
     */
    public static String runClingo() throws IOException, InterruptedException {
        Rules.createAndGetRulesFile(); //create a file from the rule enums
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream buildOutputStream = new PrintStream(outputStream);
        ProcessBuilder pb;
        if (System.getProperty("os.name").startsWith("Windows")) {
            pb = new ProcessBuilder("cmd.exe", "/c", "cd " + PATH + " && " + CMD);
        } else {
            pb = new ProcessBuilder("/bin/bash", "-c", "cd " + PATH + " ; " + CMD);
        }
        pb.directory(new File(PATH));

        Process p = pb.start();

        System.out.println("Calling clingo...");
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String content = "";
        List<String> lines = new ArrayList<>();
        String line = "";

        while ((line = reader.readLine()) != null) {
            lines.add(line);
            content = content + line + System.getProperty("line.separator");
            if (buildOutputStream != null) {
                buildOutputStream.println(line);
            }
        }
        p.waitFor();
        outputStream.flush();
        buildOutputStream.flush();
        System.out.println("  Build ended...");

        return content;
    }
}
