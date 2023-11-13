package training.quizTdd.infrastructure.api.dtos;

import java.util.List;

public record QuizResponseDto(
        String id,
        String title,
        String text,
        List<String> options
) {
}
