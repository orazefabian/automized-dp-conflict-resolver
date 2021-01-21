package dp.conflict.resolver.parse;

import dp.conflict.resolver.asp.ClingoSolver;
import dp.conflict.resolver.tree.CallNode;

import java.io.IOException;
import java.util.*;

/*********************************
 Created by Fabian Oraze on 21.01.21
 *********************************/

public class AnswerObject {

    private Map<String, Integer> idMap;
    private String stdOut;
    private Set<List<String>> answers;

    /**
     * Object that represents the answer set of a clingo command
     */
    public AnswerObject() {
        this.answers = new HashSet<>();
    }

    /**
     * calls {@link ClingoSolver} to run the clingo command with present facts.lp and rules.lp files
     *
     * @throws IOException          when reading the input files fails
     * @throws InterruptedException when clingo process gets interrupted
     */
    public void solve() throws IOException, InterruptedException {
        this.stdOut = ClingoSolver.runClingo();
        parseAnswers();
    }

    /**
     * private helper function to parse the stdOutput to a list of answers
     */
    private void parseAnswers() {
        String[] answers = this.stdOut.split("Answer: ");
        for (String ans : answers) {
            if (!ans.contains("includeJar")) continue;
            List<String> ansList = new ArrayList<>();
            this.answers.add(ansList);
            String[] includeJars = ans.split("includeJar");
            for (String jar : includeJars) {
                if (!jar.contains("(")) continue;
                int id = Integer.parseInt(jar.substring(jar.indexOf("(") + 1, jar.indexOf(")")));
                for (String key : this.idMap.keySet()) {
                    if (this.idMap.get(key) == id) {
                        ansList.add(key);
                        break;
                    }
                }
            }
        }
    }

    /**
     * set the id Map for later mapping to correct jars
     *
     * @param idMap
     */
    public void setIDMap(Map<String, Integer> idMap) {
        this.idMap = idMap;
    }

    /**
     * @return Set of lists each containing a answer set
     */
    public Set<List<String>> getAnswers() {
        return answers;
    }

    /**
     * @return the output of the clingo process
     */
    public String getStdOut() {
        return stdOut;
    }

}
