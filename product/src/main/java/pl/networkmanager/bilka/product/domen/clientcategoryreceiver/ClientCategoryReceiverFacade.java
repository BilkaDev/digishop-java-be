package pl.networkmanager.bilka.product.domen.clientcategoryreceiver;

import lombok.AllArgsConstructor;
import pl.networkmanager.bilka.product.domen.admincategorycud.AdminCategoryCudFacade;
import pl.networkmanager.bilka.product.domen.clientcategoryreceiver.dto.CategoryDto;

import java.util.List;

@AllArgsConstructor
public class ClientCategoryReceiverFacade {
    private final AdminCategoryCudFacade adminCategoryCudFacade;

    public List<CategoryDto> getCategories() {
        var categories = adminCategoryCudFacade.findAll();

        return categories.stream()
                .map(category -> CategoryDto.builder().uuid(category.shortId()).name(category.name()).build()).toList();
    }
}
