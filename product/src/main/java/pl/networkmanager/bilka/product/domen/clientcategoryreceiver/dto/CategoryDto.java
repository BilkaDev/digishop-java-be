package pl.networkmanager.bilka.product.domen.clientcategoryreceiver.dto;

import lombok.Builder;

@Builder
public record CategoryDto(String uuid, String name) {
}
