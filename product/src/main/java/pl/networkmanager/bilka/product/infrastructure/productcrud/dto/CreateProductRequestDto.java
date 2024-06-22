package pl.networkmanager.bilka.product.infrastructure.productcrud.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreateProductRequestDto(
        @NotNull
        @NotEmpty
        String name,
        @NotNull
        @NotEmpty
        String categoryShortId,
        @NotNull
        @NotEmpty
        String mainDesc,
        @NotNull
        @DecimalMin("0.0")
        BigDecimal price,
        @NotNull
        @NotEmpty
        String descHtml,
        @NotNull
        @NotEmpty
        String[] imageUrls
) {
}
