package pl.networkmanager.bilka.product.infrastructure.productcrud;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.networkmanager.bilka.product.domen.productcrud.ProductCrudFacade;
import pl.networkmanager.bilka.product.domen.productcrud.dto.CreateProductDto;
import pl.networkmanager.bilka.product.infrastructure.productcrud.dto.CreateProductRequestDto;
import pl.networkmanager.bilka.product.infrastructure.productcrud.dto.ProductResponseDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/product")
public class ProductCategoryCrudController {
    private static final Logger log = LoggerFactory.getLogger(ProductCategoryCrudController.class);
    private final ProductCrudFacade productCrudFacade;

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
        log.info("--START getProducts--");
        var products = productCrudFacade.getProduct(
                name,
                categoryShortId,
                price_min,
                price_max,
                page,
                limit,
                sort,
                order

        );
        Long count = productCrudFacade.getCountActiveProduct(
                name,
                categoryShortId,
                price_min,
                price_max
        );
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", count.toString());
        List<ProductResponseDto> productResponseDto = products.stream().map(
                v -> ProductResponseDto.builder()
                        .uuid(v.uuid())
                        .createdAt(v.createdAt())
                        .name(v.name())
                        .category(v.category())
                        .isActive(v.isActive())
                        .mainDesc(v.mainDesc())
                        .descHtml(v.descHtml())
                        .price(v.price())
                        .imageUrls(v.imageUrls())
                        .build()
        ).toList();
        log.info("--END getProducts--");
        return ResponseEntity.ok().headers(headers).body(productResponseDto);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ProductResponseDto> getProductByUuid(@PathVariable String uuid) {
        log.info("--START getProductByUuid--");
        var product = productCrudFacade.getProductByUid(uuid);
        ProductResponseDto productResponseDto = ProductResponseDto.builder()
                .uuid(product.uuid())
                .createdAt(product.createdAt())
                .name(product.name())
                .category(product.category())
                .isActive(product.isActive())
                .mainDesc(product.mainDesc())
                .descHtml(product.descHtml())
                .price(product.price())
                .imageUrls(product.imageUrls())
                .build();
        log.info("--END getProductByUuid--");
        return ResponseEntity.ok(productResponseDto);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid CreateProductRequestDto createProductRequestDto) {
        log.info("--START createProduct--");
        productCrudFacade.createProduct(CreateProductDto
                .builder()
                .name(createProductRequestDto.name())
                .categoryShortId(createProductRequestDto.categoryShortId())
                .mainDesc(createProductRequestDto.mainDesc())
                .price(createProductRequestDto.price())
                .descHtml(createProductRequestDto.descHtml())
                .imageUrls(createProductRequestDto.imageUrls())
                .build());
        log.info("--END createProduct--");
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteProduct(@PathVariable String uuid) {
        log.info("--START deleteProduct--");
        productCrudFacade.deleteProduct(uuid);
        log.info("--END deleteProduct--");
        return ResponseEntity.noContent().build();
    }

}
