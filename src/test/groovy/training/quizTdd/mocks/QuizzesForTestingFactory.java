package training.quizTdd.mocks;

import training.quizTdd.appcore.domainmodel.Quiz;

import java.util.List;

public class QuizzesForTestingFactory {

    public static Quiz createOneQuizForTests() {
        return quizNo1();
    }

    public static List<Quiz> createTwoQuizzesForTests() {
        return List.of(quizNo1(), quizNo2());
    }

    public static List<Quiz> createThreeQuizzesForTests() {
        return List.of(quizNo1(), quizNo2(), quizNo3());
    }

    public static List<Quiz> createFourQuizzesForTests() {
        return List.of(quizNo1(), quizNo2(), quizNo3(), quizNo4());
    }

    public static List<Quiz> createFiveQuizzesForTests() {
        return List.of(quizNo1(), quizNo2(), quizNo3(), quizNo4(), quizNo5());
    }

    private static Quiz quizNo1() {
        return new Quiz("quiz1", "question1", List.of("a", "b", "c"), List.of(1));
    }

    private static Quiz quizNo2() {
        return new Quiz("quiz2", "question2", List.of("a", "b", "c"), List.of(1, 0, 2));
    }

    private static Quiz quizNo3() {
        return new Quiz("quiz3", "question3", List.of("a", "b", "c"), List.of(3, 1));
    }

    private static Quiz quizNo4() {
        return new Quiz("quiz4", "question4", List.of("a", "b", "c"), List.of());
    }

    private static Quiz quizNo5() {
        return new Quiz("quiz5", "question5", List.of("a", "b", "c"), List.of(0, 1, 3));
    }

}
