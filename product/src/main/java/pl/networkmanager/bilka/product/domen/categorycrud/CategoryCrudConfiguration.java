package pl.networkmanager.bilka.product.domen.categorycrud;

public class CategoryCrudConfiguration {

    CategoryCrudFacade categoryCrudFacade(CategoryRepository categoryRepository) {
        return new CategoryCrudFacade(categoryRepository);
    }
}
