package pl.networkmanager.bilka.auth.entity;

import lombok.Builder;

@Builder
public record UserRegisterDTO(
        String login,
        String email,
        String password,
        Role role
) {
}
