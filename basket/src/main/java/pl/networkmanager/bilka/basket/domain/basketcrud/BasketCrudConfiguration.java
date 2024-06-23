package pl.networkmanager.bilka.basket.domain.basketcrud;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.networkmanager.bilka.basket.domain.basketcrud.repository.BasketItemRepository;
import pl.networkmanager.bilka.basket.domain.basketcrud.repository.BasketRepository;

@Configuration
public class BasketCrudConfiguration {
    @Bean
    public BasketCrudFacade basketCrudFacade(
            BasketRepository basketRepository,
            BasketItemRepository basketItemRepository,
            IProductRestTemplate productRestTemplate) {
        return new BasketCrudFacade(basketRepository, basketItemRepository, productRestTemplate);
    }
}
