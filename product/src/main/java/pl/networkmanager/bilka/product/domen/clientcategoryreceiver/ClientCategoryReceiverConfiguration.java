package pl.networkmanager.bilka.product.domen.clientcategoryreceiver;

import pl.networkmanager.bilka.product.domen.admincategorycud.AdminCategoryCudFacade;

public class ClientCategoryReceiverConfiguration {

    ClientCategoryReceiverFacade clientCategoryReceiverFacade(AdminCategoryCudFacade adminCategoryCudFacade) {
        return new ClientCategoryReceiverFacade(adminCategoryCudFacade);
    }
}
