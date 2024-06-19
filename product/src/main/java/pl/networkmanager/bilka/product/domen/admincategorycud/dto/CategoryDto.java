package pl.networkmanager.bilka.product.domen.admincategorycud.dto;

import lombok.Builder;

@Builder
public record CategoryDto(String name, String shortId) {
}
