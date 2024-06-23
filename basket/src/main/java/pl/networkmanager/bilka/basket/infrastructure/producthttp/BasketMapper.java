package pl.networkmanager.bilka.basket.infrastructure.producthttp;

import pl.networkmanager.bilka.basket.domain.basketcrud.dto.BasketItemAddDto;
import pl.networkmanager.bilka.basket.domain.basketcrud.dto.BasketItemDto;
import pl.networkmanager.bilka.basket.infrastructure.dto.BasketItemAddRequestDto;
import pl.networkmanager.bilka.basket.infrastructure.dto.ListBasketItemResponseDto;

import java.math.BigDecimal;
import java.util.List;

public class BasketMapper {
    static public BasketItemAddDto fromBasketItemRequestDtoToBasketItemAddDto(BasketItemAddRequestDto basketItemAddRequestDto) {
        return BasketItemAddDto.builder()
                .product(basketItemAddRequestDto.product())
                .quantity(basketItemAddRequestDto.quantity())
                .build();
    }

    static public ListBasketItemResponseDto fromListBasketItemDtoToBasketItemResponseDto(List<BasketItemDto> basketItemsDto) {
        return ListBasketItemResponseDto.builder()
                .basketProducts(basketItemsDto)
                .totalPrice(basketItemsDto.stream()
                        .map(BasketItemDto::totalPrice).reduce(BigDecimal::add).orElse(null))
                .build();
    }
}
