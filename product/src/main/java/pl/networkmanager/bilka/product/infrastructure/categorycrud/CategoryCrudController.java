package pl.networkmanager.bilka.product.infrastructure.categorycrud;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.networkmanager.bilka.product.infrastructure.categorycrud.dto.CategoryCreateRequestDto;
import pl.networkmanager.bilka.product.infrastructure.categorycrud.dto.CategoryResponseDto;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/category")
@RequiredArgsConstructor
public class CategoryCrudController {
    private final CategoryMediator categoryMediator;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getCategories() {
        return categoryMediator.getCategories();
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryCreateRequestDto categoryDto) {
        return categoryMediator.createCategory(categoryDto);
    }
}
