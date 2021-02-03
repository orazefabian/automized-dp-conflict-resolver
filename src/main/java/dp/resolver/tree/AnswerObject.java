package dp.resolver.tree;

import dp.resolver.asp.ClingoSolver;
import dp.resolver.parse.exception.NoConflictException;

import java.io.IOException;
import java.util.*;

/*********************************
 Created by Fabian Oraze on 21.01.21
 *********************************/

public class AnswerObject {

    private Map<String, Integer> idMap;
    private String stdOut;
    private Set<List<String>> answers;
    public List<String> bloatedJars;

    /**
     * Object that represents the answer set of a clingo command
     */
    public AnswerObject() {
        this.answers = new HashSet<>();
        this.bloatedJars = new ArrayList<>();
    }

    /**
     * calls {@link ClingoSolver} to run the clingo command with present facts.lp and rules.lp files
     *
     * @throws IOException          when reading the input files fails
     * @throws InterruptedException when clingo process gets interrupted
     */
    public void solve() throws IOException, InterruptedException, NoConflictException {
        this.stdOut = ClingoSolver.runClingo();
        if (this.idMap == null) throw new NoConflictException();
        parseAnswers();
    }

    /**
     * @return a list of all bloated Jars that should be removed from project
     */
    public List<String> getBloatedJars() {
        return bloatedJars;
    }

    /**
     * adds a boated jar to local list
     * @param jarPath the path to the jar
     */
    public void addBloatedJar(String jarPath){
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
