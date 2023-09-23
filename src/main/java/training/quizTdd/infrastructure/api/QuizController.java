package training.quizTdd.infrastructure.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import training.quizTdd.appcore.domainmodel.Answer;
import training.quizTdd.appcore.domainmodel.Quiz;
import training.quizTdd.appcore.domainservices.IQuizService;
import training.quizTdd.appcore.domainservices.QuizService;
import training.quizTdd.infrastructure.api.dtos.AnswerResponseDto;
import training.quizTdd.infrastructure.api.dtos.QuizRequestDto;
import training.quizTdd.infrastructure.api.dtos.QuizResponseDto;
import training.quizTdd.infrastructure.api.exceptions.QuizNotFoundException;

import java.util.List;
import java.util.Optional;

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
        QuizResponseDto quizResponseDto = new QuizResponseDto(quiz.getId(),
                quiz.getTitle(),
                quiz.getQuestion(),
                quiz.getOptions());

        return ResponseEntity.ok().body(quizResponseDto);
    }

    @GetMapping("/api/quizzes")
    public ResponseEntity<List<QuizResponseDto>> getAllQuizzes() {
        List<QuizResponseDto> quizResponseDtos = quizService.getQuizzes().stream()
                .map(quiz -> new QuizResponseDto(quiz.getId(),
                        quiz.getTitle(),
                        quiz.getQuestion(),
                        quiz.getOptions())).toList();

        return ResponseEntity.ok().body(quizResponseDtos);
    }

    @GetMapping(path = "/api/quizzes/{id}")
    public ResponseEntity<QuizResponseDto> getQuiz(@PathVariable("id") Integer id) {
        Optional<Quiz> quiz = quizService.getQuiz(id);

        if (quiz.isEmpty()) {
            throw new QuizNotFoundException("Quiz with id %d does not exist.".formatted(id));
        }

        QuizResponseDto quizResponseDto = new QuizResponseDto(quiz.get().getId(),
                quiz.get().getTitle(),
                quiz.get().getQuestion(),
                quiz.get().getOptions());

        return ResponseEntity.ok().body(quizResponseDto);
    }

    @PostMapping("/api/quizzes/{id}/solve")
    public ResponseEntity<AnswerResponseDto> solveQuiz(@PathVariable("id") @NotNull @Min(0) int id,
                                                       @RequestParam("answer") @NotNull @Min(0) int index) {
        Optional<Answer> answer = quizService.solveQuiz(id, index);

        if (answer.get().feedback().equals("-1")) {
            throw new QuizNotFoundException("Quiz with id %d does not exist.".formatted(id));
        }

        AnswerResponseDto answerResponseDto = new AnswerResponseDto(answer.get().success(), answer.get().feedback());

        return ResponseEntity.ok().body(answerResponseDto);
    }

}
