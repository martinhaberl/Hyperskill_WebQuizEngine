package training.quizTdd.infrastructure.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrationRequestDto(@NotNull @Email(message = "Enter a valid e-mail address.")
                                     String email,
                                     @Size(min = 5, message = "Enter a password with a least 5 characters")
                                     String password
) {
}