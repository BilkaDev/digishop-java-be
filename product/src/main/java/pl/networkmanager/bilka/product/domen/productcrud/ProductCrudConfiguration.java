package pl.networkmanager.bilka.product.domen.productcrud;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.networkmanager.bilka.product.domen.categorycrud.CategoryCrudFacade;

@Configuration
public class ProductCrudConfiguration {


    @Bean
    public ProductCrudFacade productCrudFacade(
            ProductRepository productRepository,
            CategoryCrudFacade categoryCrudFacade,
            EntityManager entityManager,
            IImageClientFtp imageServerFtp) {
        QueryManager queryManager = new QueryManager(entityManager, categoryCrudFacade);
        return new ProductCrudFacade(productRepository, categoryCrudFacade, queryManager, imageServerFtp);
    }

    public ProductCrudFacade createForTest(
            ProductRepository productRepository, CategoryCrudFacade categoryCrudFacade, QueryManager queryManager, IImageClientFtp imageServerFtp) {

        return new ProductCrudFacade(productRepository, categoryCrudFacade, queryManager, imageServerFtp);
    }
}
