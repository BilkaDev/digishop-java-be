package pl.networkmanager.bilka.basket.domain.dto;

import lombok.Builder;

@Builder
public record BasketItemAddDto(
        String product,
        long quantity
) {
}
