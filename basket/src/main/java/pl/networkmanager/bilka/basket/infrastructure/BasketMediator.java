package pl.networkmanager.bilka.basket.infrastructure;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.networkmanager.bilka.basket.domain.basketcrud.BasketCrudFacade;
import pl.networkmanager.bilka.basket.domain.basketcrud.dto.BasketItemAddDto;
import pl.networkmanager.bilka.basket.domain.basketcrud.dto.BasketItemDto;
import pl.networkmanager.bilka.basket.domain.basketcrud.dto.BasketDto;
import pl.networkmanager.bilka.basket.infrastructure.dto.BasketItemAddRequestDto;
import pl.networkmanager.bilka.basket.infrastructure.dto.ListBasketItemResponseDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;


@Component
@RequiredArgsConstructor
public class BasketMediator {
    private final BasketCrudFacade basketCrudFacade;
    private final CookieService cookieService;

    public ResponseEntity<?> addProduct(
            BasketItemAddRequestDto basketItemAddRequestDto,
            HttpServletRequest request,
            HttpServletResponse response) {
        HttpHeaders httpHeaders = new HttpHeaders();
        List<Cookie> cookies = new ArrayList<>();
        AtomicLong sum = new AtomicLong(0);
        if (request.getCookies() != null) {
            cookies.addAll(List.of(request.getCookies()));
        }
        cookies.stream().filter(cookie -> cookie.getName().equals("basket")).findFirst().ifPresentOrElse(
                value -> sum.set(handleBasket(value.getValue(), basketItemAddRequestDto, response)),
                () -> sum.set(handleBasket(null, basketItemAddRequestDto, response)));
        httpHeaders.add("X-Total-Count", String.valueOf(sum.get()));

        return ResponseEntity.ok().headers(httpHeaders).body(new Response("Successful add item to basket"));
    }

    public ResponseEntity<ListBasketItemResponseDto> getItems(HttpServletRequest request) {
        List<Cookie> cookies = new ArrayList<>();
        HttpHeaders httpHeaders = new HttpHeaders();
        if (request.getCookies() != null) {
            cookies.addAll(List.of(request.getCookies()));
        }
        AtomicReference<ListBasketItemResponseDto> listBasketItemResponseDto = new AtomicReference<>(
                ListBasketItemResponseDto.builder().build());
        cookies.stream().filter(value -> value.getName().equals("basket"))
                .findFirst().ifPresentOrElse(value -> {
                    BasketDto basket = basketCrudFacade.getBasket(value.getValue());
                    if (basket == null) {
                        throw new NoBasketInfoException("No basket info in request");
                    }
                    Long sum = basketCrudFacade.sumBasketItems(basket);
                    httpHeaders.add("X-Total-Count", String.valueOf(sum));
                    List<BasketItemDto> basketItems = basketCrudFacade.getBasketItems(basket);
                    listBasketItemResponseDto.set(ListBasketItemResponseDto.builder()
                            .basketProducts(basketItems)
                            .totalPrice(basketItems.stream()
                                    .map(BasketItemDto::totalPrice).reduce(BigDecimal::add).orElse(null))
                            .build());
                }, () -> {
                    throw new NoBasketInfoException("No basket info in request");
                });
        if (httpHeaders.isEmpty()) httpHeaders.add("X-Total-Count", String.valueOf(0));
        return ResponseEntity.ok().headers(httpHeaders).body(listBasketItemResponseDto.get());
    }

    private Long handleBasket(String basketUuid, BasketItemAddRequestDto basketItemAddRequestDto, HttpServletResponse response) {
        BasketDto basket = (basketUuid != null) ? basketCrudFacade.getBasket(basketUuid) : null;
        if (basket == null) {
            basket = basketCrudFacade.createBasket();
            response.addCookie(cookieService.generateCookie("basket", basket.uuid()));
        }
        basketCrudFacade.addProductToBasket(basket, BasketItemAddDto.builder()
                .product(basketItemAddRequestDto.product())
                .quantity(basketItemAddRequestDto.quantity())
                .build());
        return basketCrudFacade.sumBasketItems(basket);
    }

    public ResponseEntity<Response> delete(String uuid, HttpServletRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        List<Cookie> cookies = new ArrayList<>();
        if (request.getCookies() != null) {
            cookies.addAll(List.of(request.getCookies()));
        }
        cookies.stream().filter(value -> value.getName().equals("basket"))
                .findFirst().ifPresentOrElse(value -> {
                    Long sum = basketCrudFacade.deleteItem(uuid, value.getValue());
                    httpHeaders.add("X-Total-Count", String.valueOf(sum));
                }, () -> {
                    throw new NoBasketInfoException("No basket info in request");
                });
        return ResponseEntity.ok().headers(httpHeaders).body(new Response("Item deleted"));
    }
}
