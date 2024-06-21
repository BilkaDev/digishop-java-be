package pl.networkmanager.bilka.product.infrastructure.categorycrud;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.networkmanager.bilka.product.domen.categorycrud.CategoryCrudFacade;
import pl.networkmanager.bilka.product.domen.categorycrud.dto.CategoryCreateDto;
import pl.networkmanager.bilka.product.infrastructure.categorycrud.dto.CategoryCreateRequestDto;
import pl.networkmanager.bilka.product.infrastructure.categorycrud.dto.CategoryResponseDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/category")
@RequiredArgsConstructor
public class CategoryCrudController {
    private final CategoryCrudFacade categoryCrudFacade;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getCategories() {
        log.info("--START getCategories--");
        List<CategoryResponseDto> categories = categoryCrudFacade.getCategories()
                .stream().map(v -> CategoryResponseDto.builder()
                        .name(v.name())
                        .shortId(v.shortId())
                        .build()).toList();
        log.info("--END getCategories--");
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryCreateRequestDto categoryDto) {
        log.info("--START createCategory--");
        categoryCrudFacade.create(CategoryCreateDto.builder()
                .name(categoryDto.name())
                .build()
        );
        log.info("--END createCategory--");
        return ResponseEntity.status(201).build();
    }
}
