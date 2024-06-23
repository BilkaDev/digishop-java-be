package pl.networkmanager.bilka.basket.domain.basketcrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.networkmanager.bilka.basket.domain.basketcrud.entity.Basket;

import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    Optional<Basket> findByUuid(String uuid);
}
