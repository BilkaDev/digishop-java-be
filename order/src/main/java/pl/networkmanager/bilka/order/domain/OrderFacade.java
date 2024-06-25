package pl.networkmanager.bilka.order.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderFacade {
    private final IBasketHttpRestTemplate basketHttpRestTemplate;


}
