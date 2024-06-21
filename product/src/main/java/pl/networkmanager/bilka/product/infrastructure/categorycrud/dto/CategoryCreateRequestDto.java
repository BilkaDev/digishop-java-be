package pl.networkmanager.bilka.product.infrastructure.categorycrud.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CategoryCreateRequestDto(
        @NotNull
        @NotEmpty
        String name
) {
}
