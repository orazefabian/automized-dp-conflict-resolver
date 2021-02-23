import dp.api.maven.CentralMavenAPI;
import dp.resolver.parse.FactBuilder;
import dp.resolver.tree.AnswerObject;
import dp.resolver.tree.CallTree;
import dp.resolver.tree.ConflictType;
import dp.resolver.tree.Tree;
import org.junit.jupiter.api.*;

import java.util.*;


public class TestOnSampleProject {

    private static Tree tree;
    private static final String testProjectPath = "/Users/fabian/Projects/Sample/runtime_conflict_sample/Project_A/";
    private static AnswerObject answer;

    private static List<String> answerOne;
    private static List<String> answerTwo;
    private static List<List<String>> expectedAnswer;

    @BeforeAll
    public static void setup() {
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

        start();
    }

    private static void start() {
        try {
            tree.computeCallTree();
            FactBuilder parser;
            parser = new FactBuilder(tree.getConflicts(ConflictType.TYPE_3), tree.getNeededJars());
            answer.setIDMap(parser.getIdMap());
            answer.solve();
        } catch (Exception e) {
        }

        sortAnswer(answer);
    }

    private static void sortAnswer(AnswerObject answer) {
        for (List<String> list : answer.getAnswers()) {
            Collections.sort(list);
        }
    }

    @AfterAll
    public static void tearDown() {
        answer = null;
        tree = null;
        answerOne = null;
        answerTwo = null;
        expectedAnswer = null;
    }

    @Test
    public void testCorrectAmountBloatedJars() {
        Assertions.assertEquals(2, answer.getBloatedJars().size());
    }

    @Test
    public void testCorrectAmountAnswers() {
        Assertions.assertEquals(2, answer.getAnswers().size());
    }

    @Test
    public void testCorrectAnswerArrays() {
        Assertions.assertArrayEquals(answer.getAnswers().toArray(), expectedAnswer.toArray());
    }



}
