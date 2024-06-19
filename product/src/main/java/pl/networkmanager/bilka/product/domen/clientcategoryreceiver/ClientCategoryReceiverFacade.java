package pl.networkmanager.bilka.product.domen.clientcategoryreceiver;

import lombok.AllArgsConstructor;
import pl.networkmanager.bilka.product.domen.clientcategoryreceiver.dto.CategoryDto;

import java.util.List;

@AllArgsConstructor
public class ClientCategoryReceiverFacade {
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getCategories() {
        var categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> CategoryDto.builder().uuid(category.uuid()).name(category.name()).build()).toList();
    }
}
