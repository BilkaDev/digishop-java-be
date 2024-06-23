package pl.networkmanager.bilka.basket.infrastructure.dto;

import lombok.Builder;
import pl.networkmanager.bilka.basket.domain.basketcrud.dto.BasketItemDto;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ListBasketItemResponseDto(
        List<BasketItemDto> basketProducts,
        BigDecimal totalPrice
) {
}
