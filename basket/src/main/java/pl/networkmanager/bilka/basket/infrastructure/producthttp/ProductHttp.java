package pl.networkmanager.bilka.basket.infrastructure.producthttp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.networkmanager.bilka.basket.domain.basketcrud.IProductRestTemplate;
import pl.networkmanager.bilka.basket.domain.basketcrud.entity.Product;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class ProductHttp implements IProductRestTemplate {
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${product.service.url}")
    private String PRODUCT_URL;

    @Override
    public Product getProduct(String uuid) throws URISyntaxException {
        URI uri = new URIBuilder(PRODUCT_URL + "/getExternal").addParameter("uuid", uuid).build();
        ResponseEntity<?> response = restTemplate.getForEntity(uri, Product.class);
        if (response.getStatusCode().isError()) {
            return null;
        }
        return (Product) response.getBody();

    }
}
