package pl.networkmanager.bilka.product.infrastructure.categorycrud;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.networkmanager.bilka.product.domen.categorycrud.CategoryCrudFacade;
import pl.networkmanager.bilka.product.infrastructure.categorycrud.dto.CategoryCreateRequestDto;
import pl.networkmanager.bilka.product.infrastructure.categorycrud.dto.CategoryResponseDto;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryMediator {
    private final CategoryCrudFacade categoryCrudFacade;

    public ResponseEntity<List<CategoryResponseDto>> getCategories() {
        log.info("--START getCategories--");
        List<CategoryResponseDto> categories = categoryCrudFacade.getCategories()
                .stream().map(
                        CategoryMapper::mapFromCategoryToCategoryResponseDto
                ).toList();
        log.info("--END getCategories--");
        return ResponseEntity.ok(categories);
    }

    public ResponseEntity<?> createCategory(CategoryCreateRequestDto categoryDto) {
        log.info("--START createCategory--");
        categoryCrudFacade.create(
                CategoryMapper.mapFromCategoryCreateRequestDtoToCategoryCreateDto(categoryDto)
        );
        log.info("--END createCategory--");
        return ResponseEntity.status(201).build();
    }
}
