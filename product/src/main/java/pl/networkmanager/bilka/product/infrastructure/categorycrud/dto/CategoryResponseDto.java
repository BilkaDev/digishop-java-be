package pl.networkmanager.bilka.product.infrastructure.categorycrud.dto;


import lombok.Builder;

@Builder
public record CategoryResponseDto(String name, String shortId) {
}