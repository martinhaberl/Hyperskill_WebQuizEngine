package training.quizTdd.infrastructure.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record QuizRequestDto(@NotBlank(message = "Title should not be empty.") String title,
                             @NotBlank(message = "Question should not be empty") String text,
                             @NotNull(message = "Enter at least 2 options for solving the quiz.")
                             @Size(min = 2, message = "Enter at least 2 options for solving the quiz.")
                             List<String> options,
                             List<Integer> answer) {
}