package training.quizTdd.infrastructure.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
public class QuizController {

    @Autowired
    private final IQuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }


    @PostMapping("/api/quizzes")
    public ResponseEntity<QuizResponseDto> createQuiz(@RequestBody QuizRequestDto quizRequestDto) {
        Quiz quiz = quizService.createQuiz(quizRequestDto.title(), quizRequestDto.text(), quizRequestDto.options(), quizRequestDto.answer());
        QuizResponseDto quizResponseDto = new QuizResponseDto(quiz.getId(), quiz.getTitle(), quiz.getText(), quiz.getOptions());

        return ResponseEntity.ok().body(quizResponseDto);
    }

    @GetMapping("/api/quizzes")
    public ResponseEntity<List<QuizResponseDto>> getAllQuizzes() {
        List<QuizResponseDto> quizResponseDtos = quizService.getQuizzes().stream().map(quiz -> new QuizResponseDto(quiz.getId(), quiz.getTitle(), quiz.getText(), quiz.getOptions())).toList();

        return ResponseEntity.ok().body(quizResponseDtos);
    }

    @GetMapping(path = "/api/quizzes/{id}")
    public ResponseEntity<QuizResponseDto> getQuiz(@PathVariable("id") Integer id) {
        Optional<Quiz> quiz = quizService.getQuiz(id);

        if (quiz.isEmpty()) {
            throw new QuizNotFoundException("Quiz with id %d does not exist.".formatted(id));
        }

        QuizResponseDto quizResponseDto = new QuizResponseDto(quiz.get().getId(), quiz.get().getTitle(), quiz.get().getText(), quiz.get().getOptions());

        return ResponseEntity.ok().body(quizResponseDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/api/quizzes/{id}/solve?answer={index}")
    public ResponseEntity<AnswerResponseDto> solveQuiz(@PathVariable("id") Integer id, @RequestParam("answer") Integer index) {
        Optional<Answer> answer = quizService.solveQuiz(id, index);

        if (answer.get().feedback().equals("-1")) {
            throw new QuizNotFoundException("Quiz with id %d does not exist.".formatted(id));
        }

        AnswerResponseDto answerResponseDto = new AnswerResponseDto(answer.get().success(), answer.get().feedback());

        return ResponseEntity.ok().body(answerResponseDto);
    }


}
