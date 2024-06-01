package pl.networkmanager.bilka.auth.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
public record UserRegisterDTO(
        String login,
        String email,
        @JsonInclude(JsonInclude.Include.NON_NULL) String password,
        Role role
) {
}
