package pl.networkmanager.bilka.product.domen.productcrud;

import pl.networkmanager.bilka.product.domen.categorycrud.CategoryCrudFacade;

public class ProductCrudConfiguration {


    public ProductCrudFacade createForTest(
            ProductRepository productRepository, CategoryCrudFacade categoryCrudFacade, QueryManager queryManager, IImageClientFtp imageServerFtp) {

        return new ProductCrudFacade(productRepository, categoryCrudFacade, queryManager, imageServerFtp);
    }
}
