package pl.networkmanager.bilka.product.domen.categorycrud;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryCrudConfiguration {

    @Bean
    CategoryCrudFacade categoryCrudFacade(CategoryRepository categoryRepository) {
        return new CategoryCrudFacade(categoryRepository);
    }
}
