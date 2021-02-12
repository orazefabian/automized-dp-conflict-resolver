import dp.api.maven.CentralMavenAPI;
import dp.resolver.parse.FactBuilder;
import dp.resolver.tree.AnswerObject;
import dp.resolver.tree.CallTree;
import dp.resolver.tree.ConflictType;
import dp.resolver.tree.Tree;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

/*********************************
 Created by Fabian Oraze on 22.10.20
 *********************************/

public class TestOnSampleProject {

    private Tree tree;
    private final String testProjectPath = "/Users/fabian/Projects/Sample/runtime_conflict_sample/Project_A/";
    private AnswerObject answer;

    private List<String> answerOne;
    private List<String> answerTwo;
    private List<List<String>> expectedAnswer;

    @BeforeEach
    public void setup() {
        answer = new AnswerObject();
        CentralMavenAPI.setMaxVersionsNum(5);
        tree = new CallTree(testProjectPath, answer);

        answerOne = new ArrayList<>();
        answerOne.add("/Users/fabian/.m2/repository/org/runtime/conflict/Project_B/2.0/Project_B-2.0.jar");
        answerOne.add("/Users/fabian/.m2/repository/org/runtime/conflict/Project_C/1.0/Project_C-1.0.jar");

        answerTwo = new ArrayList<>();
        answerTwo.add("/Users/fabian/.m2/repository/org/runtime/conflict/Project_B/2.0/Project_B-2.0.jar");
        answerTwo.add("/Users/fabian/.m2/repository/org/runtime/conflict/Project_C/2.0/Project_C-2.0.jar");

        expectedAnswer = new ArrayList<>();
        expectedAnswer.add(answerOne);
        expectedAnswer.add(answerTwo);

        Collections.sort(answerOne);
        Collections.sort(answerTwo);
    }

    @AfterEach
    public void tearDown() {
        answer = null;
        tree = null;
        answerOne = null;
        answerTwo = null;
        expectedAnswer = null;
    }

    @Test
    public void testAnswerOnSampleProject() {
        try {
            tree.computeCallTree();
            FactBuilder parser;
            parser = new FactBuilder(tree.getConflicts(ConflictType.TYPE_3), tree.getNeededJars());
            answer.setIDMap(parser.getIdMap());
            answer.solve();
        } catch (Exception e) {
        }

        sortAnswer(answer);

        Assertions.assertEquals(2, answer.getBloatedJars().size());
        Assertions.assertEquals(2, answer.getAnswers().size());
        Assertions.assertArrayEquals(answer.getAnswers().toArray(), expectedAnswer.toArray());

    }

    private void sortAnswer(AnswerObject answer) {
        for (List<String> list : answer.getAnswers()) {
            Collections.sort(list);
        }
    }


}
