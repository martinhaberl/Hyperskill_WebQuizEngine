package training.quizTdd.infrastructure.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import training.quizTdd.appcore.domainmodel.Answer;
import training.quizTdd.appcore.domainmodel.Quiz;
import training.quizTdd.appcore.domainservices.IQuizService;
import training.quizTdd.infrastructure.api.dtos.AnswerResponseDto;
import training.quizTdd.infrastructure.api.dtos.QuizRequestDto;
import training.quizTdd.infrastructure.api.dtos.QuizResponseDto;
import training.quizTdd.infrastructure.api.exceptions.QuizNotFoundException;

import java.util.List;

@RestController
public class QuizController {

    private final IQuizService quizService;

    @Autowired
    public QuizController(IQuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/api/quizzes")
    public QuizResponseDto createQuiz(@RequestBody QuizRequestDto quizRequestDto) {
        Quiz quiz = quizService.createQuiz(quizRequestDto.title(), quizRequestDto.text(), quizRequestDto.options(), quizRequestDto.answer());

        return new QuizResponseDto(quiz.getId(), quiz.getTitle(), quiz.getText(), quiz.getOptions());
    }

    @PostMapping("/api/quizzes/{id}/solve?answer={index}")
    public AnswerResponseDto solveQuiz(@PathVariable Integer id, @RequestParam("answer") Integer index) {
        checkIfQuizIdExists(id);
        Answer answer = quizService.solveQuiz(id, index);

        return new AnswerResponseDto(answer.success(), answer.feedback());
    }

    @GetMapping("/api/quizzes")
    public List<QuizResponseDto> getAllQuizzes() {
        return quizService.getQuizzes().stream().map(quiz -> new QuizResponseDto(quiz.getId(), quiz.getTitle(), quiz.getText(), quiz.getOptions())).toList();
    }

    @GetMapping(path = "/api/quizzes/{id}")
    public QuizResponseDto getQuiz(@PathVariable Integer id) {
        checkIfQuizIdExists(id);
        Quiz quiz = quizService.getQuiz(id);

        return new QuizResponseDto(quiz.getId(), quiz.getTitle(), quiz.getText(), quiz.getOptions());
    }

    private void checkIfQuizIdExists(Integer id) {
        if (quizService.getQuiz(id) == null) {
            throw new QuizNotFoundException("Quiz with id %d does not exist.".formatted(id));
        }
    }

}
