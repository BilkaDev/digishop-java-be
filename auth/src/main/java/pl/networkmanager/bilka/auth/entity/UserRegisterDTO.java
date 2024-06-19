package pl.networkmanager.bilka.auth.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record UserRegisterDTO(
        @Length(min = 5, max = 50, message = "Login should be between 5 and 50 characters long")
        String login,
        @Email
        String email,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Length(min = 5, max = 50, message = "Password should be between 5 and 50 characters long")
        String password,
        Role role
) {
}
