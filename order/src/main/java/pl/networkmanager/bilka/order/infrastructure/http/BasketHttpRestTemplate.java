package pl.networkmanager.bilka.order.infrastructure.http;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.networkmanager.bilka.order.domain.IBasketHttpRestTemplate;
import pl.networkmanager.bilka.order.domain.dto.ListBasketItemDto;

@Slf4j
public class BasketHttpRestTemplate implements IBasketHttpRestTemplate {
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public ListBasketItemDto getBasket(Cookie value) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", value.getName() + "=" + value.getValue());
        ResponseEntity<ListBasketItemDto> response = restTemplate.exchange(
                BASKET_URL,
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                ListBasketItemDto.class, headers);
        if (response.getStatusCode().isError()) {
            throw new RuntimeException("Error while getting basket");
        }
        return response.getBody();
    }

    @Override
    public void removeBasket(Cookie value, String uuid) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", value.getName() + "=" + value.getValue());
        ResponseEntity<String> response = restTemplate.exchange(
                BASKET_URL + "?uuid=" + uuid,
                HttpMethod.DELETE,
                new HttpEntity<String>(headers),
                String.class);
        if (response.getStatusCode().isError()) {
            throw new RuntimeException("Error while removing basket");
        }
    }

    @Value("${basket.service.url}")
    private String BASKET_URL;

}
