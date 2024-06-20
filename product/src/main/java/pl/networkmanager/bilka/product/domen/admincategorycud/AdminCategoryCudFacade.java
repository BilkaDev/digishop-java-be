package pl.networkmanager.bilka.product.domen.admincategorycud;

import lombok.AllArgsConstructor;
import pl.networkmanager.bilka.product.domen.admincategorycud.dto.CategoryCreateDto;
import pl.networkmanager.bilka.product.domen.admincategorycud.dto.CategoryDto;
import pl.networkmanager.bilka.product.domen.admincategorycud.exception.ObjectExistInDBException;
import pl.networkmanager.bilka.product.domen.utils.ShortId;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class AdminCategoryCudFacade {
    private final CategoryRepository categoryRepository;

    public void create(CategoryCreateDto categoryCreateDto) throws ObjectExistInDBException {
        String shortUuid = ShortId.generate();
        Category category = Category.builder().name(categoryCreateDto.name()).shortId(shortUuid).build();

        categoryRepository.findByName(categoryCreateDto.name()).ifPresent(_ -> {
            throw new ObjectExistInDBException("Category exists in database with this name");
        });
        categoryRepository.save(category);
    }

    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream().map(category ->
                CategoryDto.builder().shortId(category.shortId()).name(category.name()).build()).toList();
    }

    public Optional<Category> findCategoryByShortId(String shortId) {
        return categoryRepository.findByShortId(shortId);
    }
}