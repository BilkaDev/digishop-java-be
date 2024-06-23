package pl.networkmanager.bilka.basket.domain.basketcrud;

import pl.networkmanager.bilka.basket.domain.basketcrud.entity.Product;

import java.net.URISyntaxException;

public interface IProductRestTemplate {
    Product getProduct(String product) throws URISyntaxException;
}
