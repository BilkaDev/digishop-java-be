package pl.networkmanager.bilka.product.infrastructure.productcrud;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.networkmanager.bilka.product.infrastructure.productcrud.dto.CreateProductRequestDto;
import pl.networkmanager.bilka.product.infrastructure.productcrud.dto.ProductResponseDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/product")
public class ProductCrudController {
    private final ProductMediator productMediator;

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String categoryShortId,
            @RequestParam(required = false) Float price_min,
            @RequestParam(required = false) Float price_max,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "price") String sort,
            @RequestParam(required = false, defaultValue = "asc") String order
    ) {
        return productMediator.getProducts(name, categoryShortId, price_min, price_max, page, limit, sort, order);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ProductResponseDto> getProductByUuid(@PathVariable String uuid) {
        return productMediator.getOneProductByUuid(uuid);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid CreateProductRequestDto createProductRequestDto) {
        return productMediator.createProduct(createProductRequestDto);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteProduct(@PathVariable String uuid) {
        return productMediator.deleteProduct(uuid);
    }

}
