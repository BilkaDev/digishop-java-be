package pl.networkmanager.bilka.product.domen.productcrud;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByUid(String uid);
}
