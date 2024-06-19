package pl.networkmanager.bilka.product.domen.clientcategoryreceiver.dto;

import lombok.Builder;

@Builder
public record CategoryDto(String shortId, String name) {
}
