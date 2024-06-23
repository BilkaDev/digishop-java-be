package pl.networkmanager.bilka.basket.domain.basketcrud;

import pl.networkmanager.bilka.basket.domain.basketcrud.entity.Product;

public interface IProductRestTemplate {
    Product getProduct(String product);
}
