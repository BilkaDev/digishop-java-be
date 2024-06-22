package pl.networkmanager.bilka.product.domen.productcrud.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreateProductDto(
        String name,
        String categoryShortId,
        String mainDesc,
        BigDecimal price,
        String descHtml,
        String[] imageUrls
) {
}
