package pl.networkmanager.bilka.basket.domain.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ListBasketItemDto(
        List<BasketItemDto> basketProducts,
        BigDecimal totalPrice
) {
}
