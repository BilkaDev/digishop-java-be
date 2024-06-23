package pl.networkmanager.bilka.basket.domain.basketcrud.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BasketItemDto(
        String uuid,
        String name,
        long quantity,
        String imageUrl,
        BigDecimal price,
        BigDecimal totalPrice
) {
}
