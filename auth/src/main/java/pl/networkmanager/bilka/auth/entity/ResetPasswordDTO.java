package pl.networkmanager.bilka.auth.entity;

import lombok.Builder;

@Builder
public record ResetPasswordDTO(String email) {
}
