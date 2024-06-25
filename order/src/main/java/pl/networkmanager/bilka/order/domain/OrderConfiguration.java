package pl.networkmanager.bilka.order.domain;

import org.springframework.context.annotation.Bean;

public class OrderConfiguration {

    @Bean
    public OrderFacade orderFacade(IBasketHttpRestTemplate basketHttpRestTemplate) {
        return new OrderFacade(basketHttpRestTemplate);
    }
}
