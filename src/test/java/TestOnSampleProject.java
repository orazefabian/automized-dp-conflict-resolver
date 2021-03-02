import dp.api.maven.CentralMavenAPI;
import dp.resolver.parse.FactBuilder;
import dp.resolver.tree.AnswerObject;
import dp.resolver.tree.CallTree;
import dp.resolver.tree.ConflictType;
import dp.resolver.tree.Tree;
import dp.resolver.tree.element.CallNode;
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
        answerOne.add("/Users/fabian/.m2/repository/org/runtime/conflict/Project_C/2.0/Project_C-2.0.jar");

        answerTwo = new ArrayList<>();
        answerTwo.add("/Users/fabian/.m2/repository/org/runtime/conflict/Project_B/2.0/Project_B-2.0.jar");
        answerTwo.add("/Users/fabian/.m2/repository/org/runtime/conflict/Project_C/1.0/Project_C-1.0.jar");

        expectedAnswer = new ArrayList<>();
        expectedAnswer.add(answerTwo);
        expectedAnswer.add(answerOne);

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
        Assertions.assertArrayEquals(expectedAnswer.toArray(), answer.getAnswers().toArray());
    }

    @Test
    public void testCorrectConflictNodes() {
        List<CallNode> conflicts = tree.getConflicts(ConflictType.TYPE_3);

        Assertions.assertEquals("conflict.ExtraObject_D", conflicts.get(0).getClassName());
        Assertions.assertTrue(conflicts.get(0).getFromJar().endsWith("3.0.jar"));

        Assertions.assertEquals("conflict.Object_D", conflicts.get(1).getClassName());
        Assertions.assertTrue(conflicts.get(1).getFromJar().endsWith("2.0.jar"));

        Assertions.assertEquals("conflict.Object_D", conflicts.get(2).getClassName());
        Assertions.assertTrue(conflicts.get(2).getFromJar().endsWith("3.0.jar"));
    }


}
