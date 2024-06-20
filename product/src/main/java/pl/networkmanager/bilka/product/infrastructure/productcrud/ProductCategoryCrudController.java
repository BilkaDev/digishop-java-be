package pl.networkmanager.bilka.product.infrastructure.productcrud;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.networkmanager.bilka.product.domen.productcrud.ProductCrudFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/product")
public class ProductCategoryCrudController {
    private final ProductCrudFacade productCrudFacade;

    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String categoryShortId,
            @RequestParam(required = false) Float price_min,
            @RequestParam(required = false) Float price_max,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "price") String sort,
            @RequestParam(required = false, defaultValue = "asc") String order
    ) {
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
        return ResponseEntity.ok(products);
    }
}
