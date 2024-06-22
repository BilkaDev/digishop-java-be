package pl.networkmanager.bilka.product.domen.categorycrud;

import lombok.AllArgsConstructor;
import pl.networkmanager.bilka.product.domen.categorycrud.dto.CategoryCreateDto;
import pl.networkmanager.bilka.product.domen.categorycrud.dto.CategoryDto;
import pl.networkmanager.bilka.product.domen.common.exception.ObjectExistInDBException;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class CategoryCrudFacade {
    private final CategoryRepository categoryRepository;

    public void create(CategoryCreateDto categoryCreateDto) throws ObjectExistInDBException {
        Category category = Category.builder().name(categoryCreateDto.name()).build();

        categoryRepository.findByName(categoryCreateDto.name()).ifPresent(_ -> {
            throw new ObjectExistInDBException("Category exists in database with this name");
        });
        categoryRepository.save(category);
    }

    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll().stream().map(category ->
                        CategoryDto
                                .builder()
                                .shortId(category.getShortId())
                                .name(category.getName())
                                .build())
                .toList();
    }

    public Optional<Category> findCategoryByShortId(String shortId) {
        return categoryRepository.findByShortId(shortId);
    }
}
