package pl.networkmanager.bilka.basket.domain.basketcrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.networkmanager.bilka.basket.domain.basketcrud.entity.Basket;
import pl.networkmanager.bilka.basket.domain.basketcrud.entity.BasketItems;

import java.util.List;
import java.util.Optional;

public interface BasketItemRepository extends JpaRepository<BasketItems, Long> {
    Optional<BasketItems> findByBasketUuidAndProduct(String basketUuid, String product);

    @Query(nativeQuery = true ,value = "SELECT SUM(quantity) from basket_items where basket = ?1")
    Long sumBasketItems(long basket);
    Optional<BasketItems> findBasketItemsByProductAndBasket(String uuid,Basket basket);
    List<BasketItems> findBasketItemsByBasket(Basket basket);
}

