package pl.networkmanager.bilka.product.infrastructure.productcrud;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import pl.networkmanager.bilka.product.domen.productcrud.ProductCrudFacade;
import pl.networkmanager.bilka.product.infrastructure.productcrud.dto.CreateProductRequestDto;
import pl.networkmanager.bilka.product.infrastructure.productcrud.dto.ProductResponseDto;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ProductMediator {
    private final ProductCrudFacade productCrudFacade;

    ResponseEntity<List<ProductResponseDto>> getProducts(
            String name,
            String categoryShortId,
            Float price_min,
            Float price_max,
            Integer page,
            Integer limit,
            String sort,
            String order
    ) {
        log.info("--START getProducts--");
        if (name != null && !name.isEmpty()) {
            try {
                name = URLDecoder.decode(name, StandardCharsets.UTF_8);
            } catch (Exception e) {
                throw new RuntimeException("Error while decoding name");
            }
        }
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
                ProductMapper::mapFromProductDtoToProductResponseDto
        ).toList();
        log.info("--END getProducts--");
        return ResponseEntity.ok().headers(headers).body(productResponseDto);
    }

    ResponseEntity<ProductResponseDto> getOneProductByUuid(String uuid) {
        log.info("--START getOneProductByUuid--");
        var product = productCrudFacade.getProductByUid(uuid);
        ProductResponseDto productResponseDto = ProductMapper.mapFromProductDtoToProductResponseDto(product);
        log.info("--END getOneProductByUuid--");
        return ResponseEntity.ok(productResponseDto);
    }

    public ResponseEntity<?> deleteProduct(String uuid) {
        log.info("--START deleteProduct--");
        productCrudFacade.deleteProduct(uuid);
        log.info("--END deleteProduct--");
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> createProduct(CreateProductRequestDto createProductDto) {
        log.info("--START createProduct--");
        productCrudFacade.createProduct(
                ProductMapper.mapFromCreateProductRequestDtoToCreateProductDto(createProductDto)
        );
        log.info("--END createProduct--");
        return ResponseEntity.status(201).build();
    }
}
