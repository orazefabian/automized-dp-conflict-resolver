package dp.resolver.asp;

import dp.resolver.parse.exception.NoConflictException;

import java.io.IOException;
import java.util.*;

/*********************************
 Created by Fabian Oraze on 21.01.21
 *********************************/

public class AnswerSetData {

    private Map<String, Integer> idMap;
    private String stdOut;
    private List<List<String>> answers;
    public Set<String> bloatedJars;

    /**
     * Object that represents the answer set of a clingo command
     */
    public AnswerSetData() {
        this.answers = new ArrayList<>();
        this.bloatedJars = new HashSet<>();
    }

    /**
     * calls {@link ClingoSolver} to run the clingo command with present facts.lp and rules.lp files and parses the answer upon completion
     *
     * @throws IOException          when reading the input files fails
     * @throws InterruptedException when clingo process gets interrupted
     * @throws NoConflictException  when the idMap is null (this means the previous FactBuilder was given no conflicts)
     */
    public void solve() throws IOException, InterruptedException, NoConflictException {
        this.stdOut = ClingoSolver.runClingo();
        if (this.idMap == null) throw new NoConflictException();
        parseAnswers();
    }

    /**
     * @return a list of all bloated Jars that should be removed from project
     */
    public Set<String> getBloatedJars() {
        return bloatedJars;
    }

    /**
     * adds a boated jar to local list
     *
     * @param jarPath the path to the jar
     */
    public void addBloatedJar(String jarPath) {
        this.bloatedJars.add(jarPath);
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
    public List<List<String>> getAnswers() {
        return answers;
    }

    /**
     * @return the output of the clingo process
     */
    public String getStdOut() {
        return stdOut;
    }


    public void addAllBloatedJars(List<String> jarsToRemove) {
        this.bloatedJars.addAll(jarsToRemove);
    }
}
