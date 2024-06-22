package pl.networkmanager.bilka.product.domen.productcrud.dto;

import lombok.Builder;
import pl.networkmanager.bilka.product.domen.categorycrud.dto.CategoryDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
public record ProductDto(
        String uuid,
        LocalDate createdAt,
        String name,
        CategoryDto category,
        Boolean isActive,
        String mainDesc,
        String descHtml,
        BigDecimal price,
        List<String> imageUrls
) {
}