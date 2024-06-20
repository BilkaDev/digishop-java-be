package pl.networkmanager.bilka.product.domen.productcrud.dto;

import lombok.Builder;
import pl.networkmanager.bilka.product.domen.categorycrud.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
public record ProductDto(
        String uid,
        LocalDate createdAt,
        String name,
        Category category,
        Boolean isActive,
        String mainDesc,
        String descHtml,
        BigDecimal price,
        List<String> imageUrls
) {
}