package pl.networkmanager.bilka.basket.infrastructure.dto;

import lombok.Builder;

@Builder
public record BasketItemAddRequestDto(
        String product,
        int quantity
) {
}
