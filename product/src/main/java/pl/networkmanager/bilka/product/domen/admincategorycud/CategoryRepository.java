package pl.networkmanager.bilka.product.domen.admincategorycud;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    Optional<Category> findByShortId(String shortId);
}
