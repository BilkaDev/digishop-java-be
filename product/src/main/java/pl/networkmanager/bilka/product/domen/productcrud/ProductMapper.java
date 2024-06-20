package pl.networkmanager.bilka.product.domen.productcrud;

import pl.networkmanager.bilka.product.domen.categorycrud.Category;
import pl.networkmanager.bilka.product.domen.productcrud.dto.CreateProductDto;

import java.util.Arrays;
import java.util.HashSet;

public class ProductMapper {
    static public Product mapFromCreateProductDtoToProduct(CreateProductDto createProductDto, Category category) {
        Product product = new Product();
        product.setCategory(category);
        product.setName(createProductDto.name());
        product.setMainDesc(createProductDto.mainDesc());
        product.setDescHtml(createProductDto.descHtml());
        product.setPrice(createProductDto.price());
        product.setImageUrls(
                new HashSet<>(Arrays.asList(createProductDto.imageUrls()))
        );
        return product;
    }
}
