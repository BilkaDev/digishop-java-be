package pl.networkmanager.bilka.product.domen.categorycrud.dto;

import lombok.Builder;

@Builder
public record CategoryDto(String name, String shortId) {
}
