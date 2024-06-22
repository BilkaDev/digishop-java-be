package pl.networkmanager.bilka.basket.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.networkmanager.bilka.basket.domain.entity.Basket;

public interface BasketRepository extends JpaRepository<Basket, Long> {
}
