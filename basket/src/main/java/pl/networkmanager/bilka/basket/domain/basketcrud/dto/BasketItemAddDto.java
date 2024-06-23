package pl.networkmanager.bilka.basket.domain.basketcrud.dto;

import lombok.Builder;

@Builder
public record BasketItemAddDto(
        String product,
        int quantity
) {
}
