package pl.networkmanager.bilka.order.domain;

import jakarta.servlet.http.Cookie;
import pl.networkmanager.bilka.order.domain.dto.ListBasketItemDto;

public interface IBasketHttpRestTemplate {
    ListBasketItemDto getBasket(Cookie value);

    void removeBasket(Cookie value, String uuid);
}
