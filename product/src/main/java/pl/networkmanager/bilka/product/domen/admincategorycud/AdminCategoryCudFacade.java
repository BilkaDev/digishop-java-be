package pl.networkmanager.bilka.product.domen.admincategorycud;

import lombok.AllArgsConstructor;
import pl.networkmanager.bilka.product.domen.admincategorycud.dto.CategoryCreateDto;
import pl.networkmanager.bilka.product.domen.admincategorycud.dto.CategoryDto;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class AdminCategoryCudFacade {
    private final CategoryRepository categoryRepository;

    public void create(CategoryCreateDto categoryCreateDto) {
        String shortUuid = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        Category category = Category.builder().name(categoryCreateDto.name()).shortId(shortUuid).build();
        categoryRepository.save(category);

        categoryRepository.findByName(categoryCreateDto.name()).ifPresent(_ -> {
            throw new RuntimeException("Category with name " + " already exists");
        });
    }

    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream().map(category ->
                CategoryDto.builder().shortId(category.shortId()).name(category.name()).build()).toList();
    }
}
