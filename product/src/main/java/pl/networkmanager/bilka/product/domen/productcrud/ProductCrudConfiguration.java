package pl.networkmanager.bilka.product.domen.productcrud;

import pl.networkmanager.bilka.product.domen.categorycrud.CategoryCrudFacade;

public class ProductCrudConfiguration {

    public ProductCrudFacade productCrudFacade(ProductRepository productRepository, CategoryCrudFacade categoryCrudFacade) {
        return new ProductCrudFacade(productRepository, categoryCrudFacade);
    }
}
