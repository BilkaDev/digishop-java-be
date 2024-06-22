package pl.networkmanager.bilka.basket.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.networkmanager.bilka.basket.domain.entity.BasketItems;

public interface BasketItemRepository extends JpaRepository<BasketItems, Long> {
}
