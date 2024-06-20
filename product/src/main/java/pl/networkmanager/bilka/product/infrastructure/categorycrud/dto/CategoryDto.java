package pl.networkmanager.bilka.product.infrastructure.categorycrud.dto;


import lombok.Builder;

@Builder
public record CategoryDto(String name, String shortId) {
}