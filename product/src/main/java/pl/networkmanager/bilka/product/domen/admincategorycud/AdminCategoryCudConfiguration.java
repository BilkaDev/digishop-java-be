package pl.networkmanager.bilka.product.domen.admincategorycud;

public class AdminCategoryCudConfiguration {

    AdminCategoryCudFacade adminCategoryCudFacade(CategoryRepository categoryRepository) {
        return new AdminCategoryCudFacade(categoryRepository);
    }
}
