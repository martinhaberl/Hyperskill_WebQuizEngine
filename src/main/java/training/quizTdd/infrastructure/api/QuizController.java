package training.quizTdd.infrastructure.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import training.quizTdd.appcore.domainmodel.Answer;
import training.quizTdd.appcore.domainmodel.Quiz;
import training.quizTdd.appcore.domainservices.IQuizService;
import training.quizTdd.appcore.domainservices.QuizService;
import training.quizTdd.infrastructure.api.dtos.AnswerResponseDto;
import training.quizTdd.infrastructure.api.dtos.AnswersRequestDto;
import training.quizTdd.infrastructure.api.dtos.QuizRequestDto;
import training.quizTdd.infrastructure.api.dtos.QuizResponseDto;
import training.quizTdd.infrastructure.api.exceptions.QuizNotFoundException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Controller
@Validated
public class QuizController {

    @Autowired
    private final IQuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/api/quizzes")
    public ResponseEntity<QuizResponseDto> createQuiz(@RequestBody @Valid QuizRequestDto quizRequestDto) {
        final List<Integer> answerInput = quizRequestDto.answer() == null ||
                quizRequestDto.answer().isEmpty() ? List.of() : quizRequestDto.answer();

        Quiz quiz = quizService.createQuiz(quizRequestDto.title(),
                quizRequestDto.text(),
                quizRequestDto.options(),
                answerInput);
        QuizResponseDto quizResponseDto = new QuizResponseDto(quiz.getId().toString(),
                quiz.getTitle(),
                quiz.getText(),
                quiz.getOptions());

        return ResponseEntity.ok().body(quizResponseDto);
    }

    @GetMapping("/api/quizzes")
    public ResponseEntity<List<QuizResponseDto>> getAllQuizzes() {
        List<QuizResponseDto> quizResponseDtos = quizService.getQuizzes().stream()
                .map(quiz -> new QuizResponseDto(quiz.getId().toString(),
                        quiz.getTitle(),
                        quiz.getText(),
                        quiz.getOptions())).toList();

        return ResponseEntity.ok().body(quizResponseDtos);
    }

    @GetMapping(path = "/api/quizzes/{id}")
    public ResponseEntity<QuizResponseDto> getQuiz(@PathVariable("id") UUID id) {
        try {
            Quiz quiz = quizService.getQuiz(id);

            QuizResponseDto quizResponseDto = new QuizResponseDto(quiz.getId().toString(),
                    quiz.getTitle(),
                    quiz.getText(),
                    quiz.getOptions());

            return ResponseEntity.ok().body(quizResponseDto);
        } catch (NoSuchElementException e) {
            throw getQuizNotFoundException(id.toString());
        }
    }

    @PostMapping("/api/quizzes/{id}/solve")
    public ResponseEntity<AnswerResponseDto> solveQuiz(@PathVariable("id") @NotNull UUID quizId,
                                                       @RequestBody AnswersRequestDto answersRequestDto) {
        try {
            Answer answer = quizService.solveQuiz(quizId, answersRequestDto.answer());

            if (answer.feedback().equals("Quiz does not exist.")) {
                throw getQuizNotFoundException(quizId.toString());
            }

            AnswerResponseDto answerResponseDto = new AnswerResponseDto(answer.success(),
                    answer.feedback());

            return ResponseEntity.ok().body(answerResponseDto);
        } catch (NoSuchElementException e) {
            throw getQuizNotFoundException(quizId.toString());
        }
    }

    private static QuizNotFoundException getQuizNotFoundException(final String quizId) {
        return new QuizNotFoundException("Quiz with id %s does not exist.".formatted(quizId));
    }
}
