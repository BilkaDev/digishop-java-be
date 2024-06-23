package pl.networkmanager.bilka.basket.domain.basketcrud;

import lombok.RequiredArgsConstructor;
import pl.networkmanager.bilka.basket.domain.basketcrud.dto.BasketDto;
import pl.networkmanager.bilka.basket.domain.basketcrud.dto.BasketItemAddDto;
import pl.networkmanager.bilka.basket.domain.basketcrud.dto.BasketItemDto;
import pl.networkmanager.bilka.basket.domain.basketcrud.entity.Basket;
import pl.networkmanager.bilka.basket.domain.basketcrud.entity.BasketItems;
import pl.networkmanager.bilka.basket.domain.basketcrud.entity.Product;
import pl.networkmanager.bilka.basket.domain.basketcrud.repository.BasketItemRepository;
import pl.networkmanager.bilka.basket.domain.basketcrud.repository.BasketRepository;
import pl.networkmanager.bilka.basket.domain.exceptions.BasketDontExistException;
import pl.networkmanager.bilka.basket.domain.exceptions.ProductDontExistsException;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class BasketCrudFacade {
    private final BasketRepository basketRepository;
    private final BasketItemRepository basketItemRepository;
    private final IProductRestTemplate productRestTemplate;


    public BasketDto getBasket(String uuid) {
        Basket basket = basketRepository.findByUuid(uuid).orElse(null);
        if (basket == null) {
            return null;
        }
        return BasketDto.builder()
                .uuid(basket.getUuid())
                .build();
    }

    public List<BasketItemDto> getBasketItems(BasketDto basketDto) {
        Basket basket = basketRepository.findByUuid(basketDto.uuid()).orElse(null);
        if (basket == null) {
            return new ArrayList<>();
        }
        return basketItemRepository.findBasketItemsByBasket(basket).stream()
                .map(item -> {
                            try {
                                Product product = productRestTemplate.getProduct(item.getProduct());
                                return BasketItemDto.builder()
                                        .uuid(item.getUuid())
                                        .name(product.getName())
                                        .quantity(item.getQuantity())
                                        .imageUrl(product.getImageUrls()[0])
                                        .price(product.getPrice())
                                        .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                                        .build();

                            } catch (URISyntaxException e) {
                                throw new RuntimeException("Product not found");
                            }
                        }
                )
                .collect(Collectors.toList());
    }

    public BasketDto createBasket() {
        Basket basket = new Basket();
        basketRepository.save(basket);
        return BasketDto.builder()
                .uuid(basket.getUuid())
                .build();

    }

    public void addProductToBasket(BasketDto basketDto, BasketItemAddDto basketItemAddDTO) {
        try {

            BasketItems basketItems = new BasketItems();
            Basket basket = basketRepository.findByUuid(basketDto.uuid()).orElse(null);

            Product product = productRestTemplate.getProduct(basketItemAddDTO.product());
            if (product != null && basket != null) {
                basketItemRepository.findByBasketUuidAndProduct(basketDto.uuid(), product.getUuid()).ifPresentOrElse(basketItems1 -> {
                    basketItems1.setQuantity(basketItems1.getQuantity() + basketItemAddDTO.quantity());
                    basketItemRepository.save(basketItems1);
                }, () -> {
                    basketItems.setBasket(basket);
                    basketItems.setQuantity(basketItemAddDTO.quantity());
                    basketItems.setProduct(product.getUuid());
                    basketItemRepository.save(basketItems);
                });
            } else {
                throw new ProductDontExistsException("Product not found");
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException("Product not found");
        }
    }

    public Long deleteItem(String uuid, String basketUuid) {
        Basket basket = basketRepository.findByUuid(basketUuid).orElse(null);
        if (basket == null) {
            throw new BasketDontExistException("Basket not found");
        }
        basketItemRepository.findBasketItemsByProductAndBasket(uuid, basket).ifPresentOrElse(basketItemRepository::delete, () -> {
            throw new BasketDontExistException("Basket item dont exist");
        });
        Long sum = basketItemRepository.sumBasketItems(basket.getId());
        if (sum == null || sum == 0) {
            sum = 0L;
            basketRepository.delete(basket);
        }
        return sum;
    }


    public Long sumBasketItems(BasketDto basketDto) {
        Basket basket = basketRepository.findByUuid(basketDto.uuid()).orElse(null);
        if (basket == null) {
            return 0L;
        }
        Long sum = basketItemRepository.sumBasketItems(basket.getId());
        if (sum == null) {
            return 0L;
        }
        return sum;
    }
}
