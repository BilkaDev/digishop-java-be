package pl.networkmanager.bilka.product.infrastructure.categorycrud;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.networkmanager.bilka.product.domen.categorycrud.CategoryCrudFacade;
import pl.networkmanager.bilka.product.infrastructure.categorycrud.dto.CategoryDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/category")
@RequiredArgsConstructor
public class CategoryCrudController {
    private final CategoryCrudFacade categoryCrudFacade;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
        log.info("--START getCategories--");
        List<CategoryDto> categories = categoryCrudFacade.getCategories()
                .stream().map(v -> CategoryDto.builder()
                        .name(v.name())
                        .shortId(v.shortId())
                        .build()).toList();
        log.info("--END getCategories--");
        return ResponseEntity.ok(categories);
    }
}
