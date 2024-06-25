package pl.networkmanager.bilka.order.domain.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ListBasketItemDto(
        List<BasketItemDto> basketProducts,
        BigDecimal totalPrice
) {
}
