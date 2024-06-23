package pl.networkmanager.bilka.basket.infrastructure.producthttp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.networkmanager.bilka.basket.domain.basketcrud.IProductRestTemplate;

@Configuration
public class ProductHttpConfiguration {

    @Bean
    public IProductRestTemplate productRestTemplate() {
        return new ProductHttp();
    }
}
