package pl.networkmanager.bilka.basket.domain.basketcrud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.networkmanager.bilka.basket.domain.basketcrud.dto.BasketItemAddDto;
import pl.networkmanager.bilka.basket.domain.basketcrud.dto.BasketItemDto;
import pl.networkmanager.bilka.basket.domain.basketcrud.entity.Basket;
import pl.networkmanager.bilka.basket.domain.basketcrud.entity.BasketItems;
import pl.networkmanager.bilka.basket.domain.basketcrud.entity.Product;
import pl.networkmanager.bilka.basket.domain.BasketDontExistException;
import pl.networkmanager.bilka.basket.domain.basketcrud.repository.BasketItemRepository;
import pl.networkmanager.bilka.basket.domain.basketcrud.repository.BasketRepository;
import pl.networkmanager.bilka.basket.domain.dto.BasketDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BasketCrudFacadeTest {

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private BasketItemRepository basketItemRepository;

    @Mock
    private IProductRestTemplate productRestTemplate;

    private BasketCrudFacade basketCrudFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        basketCrudFacade = new BasketCrudFacade(basketRepository, basketItemRepository, productRestTemplate);
    }

    @Test
    void should_return_basket_when_valid_uuid_is_given() {
        String uuid = UUID.randomUUID().toString();
        Basket basket = new Basket();
        basket.setUuid(uuid);
        when(basketRepository.findByUuid(uuid)).thenReturn(Optional.of(basket));

        BasketDto result = basketCrudFacade.getBasket(uuid);

        assertEquals(uuid, result.uuid());
    }

    @Test
    void should_return_null_when_invalid_uuid_is_given() {
        String uuid = UUID.randomUUID().toString();
        when(basketRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        BasketDto result = basketCrudFacade.getBasket(uuid);

        assertNull(result);
    }

    @Test
    void should_create_basket_successfully() {
        when(basketRepository.save(any(Basket.class))).thenReturn(new Basket());

        BasketDto result = basketCrudFacade.createBasket();
        assertThat(result.uuid()).hasSize(36);
    }

    @Test
    void should_add_product_to_basket_successfully() {
        String uuid = UUID.randomUUID().toString();
        Basket basket = new Basket();
        basket.setUuid(uuid);
        Product product = new Product();
        product.setUuid(UUID.randomUUID().toString());
        BasketItemAddDto basketItemAddDto = new BasketItemAddDto(product.getUuid(), 1);
        BasketDto basketDto = new BasketDto(basket.getUuid());

        when(basketRepository.findByUuid(basketDto.uuid())).thenReturn(Optional.of(basket));
        when(productRestTemplate.getProduct(basketItemAddDto.product())).thenReturn(product);

        basketCrudFacade.addProductToBasket(basketDto, basketItemAddDto);

        verify(basketItemRepository, times(1)).save(any(BasketItems.class));
    }

    @Test
    void should_return_sum_of_basket_items() {
        String uuid = UUID.randomUUID().toString();
        Basket basket = new Basket();
        basket.setId(1L);
        basket.setUuid(uuid);
        BasketDto basketDto = new BasketDto(basket.getUuid());

        when(basketRepository.findByUuid(basketDto.uuid())).thenReturn(Optional.of(basket));
        when(basketItemRepository.sumBasketItems(basket.getId())).thenReturn(10L);

        Long result = basketCrudFacade.sumBasketItems(basketDto);

        assertEquals(10L, result);
    }

    @Test
    void should_return_basket_items_when_valid_basket_is_given() {
        String uuid = UUID.randomUUID().toString();
        Basket basket = new Basket();
        basket.setUuid(uuid);
        BasketDto basketDto = new BasketDto(basket.getUuid());
        BasketItems basketItems = new BasketItems();
        basketItems.setBasket(basket);
        basketItems.setUuid(UUID.randomUUID().toString());
        basketItems.setQuantity(1);
        basketItems.setProduct(UUID.randomUUID().toString());
        List<BasketItems> basketItemsList = new ArrayList<>();
        basketItemsList.add(basketItems);
        Product product = new Product();
        product.setUuid(basketItems.getProduct());
        product.setName("Product Name");
        product.setPrice(BigDecimal.valueOf(100));
        product.setImageUrls(new String[]{"http://image.url"});

        when(basketRepository.findByUuid(basketDto.uuid())).thenReturn(Optional.of(basket));
        when(basketItemRepository.findBasketItemsByBasket(basket)).thenReturn(basketItemsList);
        when(productRestTemplate.getProduct(basketItems.getProduct())).thenReturn(product);

        List<BasketItemDto> result = basketCrudFacade.getBasketItems(basketDto);

        assertEquals(1, result.size());
        assertEquals(basketItems.getUuid(), result.getFirst().uuid());
        assertEquals(product.getName(), result.getFirst().name());
        assertEquals(product.getImageUrls()[0], result.getFirst().imageUrl());
        assertEquals(product.getPrice().multiply(BigDecimal.valueOf(basketItems.getQuantity())), result.getFirst().totalPrice());
    }

    @Test
    void should_return_empty_list_when_basket_does_not_exist() {
        String uuid = UUID.randomUUID().toString();
        BasketDto basketDto = new BasketDto(uuid);

        when(basketRepository.findByUuid(basketDto.uuid())).thenReturn(Optional.empty());

        List<BasketItemDto> result = basketCrudFacade.getBasketItems(basketDto);

        assertTrue(result.isEmpty());
    }

    @Test
    void should_return_empty_list_when_no_basket_items_exist() {
        String uuid = UUID.randomUUID().toString();
        Basket basket = new Basket();
        basket.setUuid(uuid);
        BasketDto basketDto = new BasketDto(basket.getUuid());

        when(basketRepository.findByUuid(basketDto.uuid())).thenReturn(Optional.of(basket));
        when(basketItemRepository.findBasketItemsByBasket(basket)).thenReturn(new ArrayList<>());

        List<BasketItemDto> result = basketCrudFacade.getBasketItems(basketDto);

        assertTrue(result.isEmpty());
    }

    @Test
    void should_delete_item_when_basket_and_item_exist() {
        String uuid = UUID.randomUUID().toString();
        String basketUuid = UUID.randomUUID().toString();
        Basket basket = new Basket();
        basket.setId(1L);
        basket.setUuid(basketUuid);
        BasketItems basketItems = new BasketItems();
        basketItems.setId(1L);
        basketItems.setUuid(uuid);
        basketItems.setBasket(basket);

        when(basketRepository.findByUuid(basketUuid)).thenReturn(Optional.of(basket));
        when(basketItemRepository.findBasketItemsByProductAndBasket(uuid, basket)).thenReturn(Optional.of(basketItems));

        Long result = basketCrudFacade.deleteItem(uuid, basketUuid);

        verify(basketItemRepository, times(1)).delete(basketItems);
        assertEquals(0L, result);
    }

    @Test
    void should_throw_exception_when_basket_does_not_exist() {
        String uuid = UUID.randomUUID().toString();
        String basketUuid = UUID.randomUUID().toString();

        when(basketRepository.findByUuid(basketUuid)).thenReturn(Optional.empty());

        assertThrows(BasketDontExistException.class, () -> basketCrudFacade.deleteItem(uuid, basketUuid));
    }

    @Test
    void should_throw_exception_when_basket_item_does_not_exist() {
        String uuid = UUID.randomUUID().toString();
        String basketUuid = UUID.randomUUID().toString();
        Basket basket = new Basket();
        basket.setUuid(basketUuid);

        when(basketRepository.findByUuid(basketUuid)).thenReturn(Optional.of(basket));
        when(basketItemRepository.findBasketItemsByProductAndBasket(uuid, basket)).thenReturn(Optional.empty());

        assertThrows(BasketDontExistException.class, () -> basketCrudFacade.deleteItem(uuid, basketUuid));
    }
}