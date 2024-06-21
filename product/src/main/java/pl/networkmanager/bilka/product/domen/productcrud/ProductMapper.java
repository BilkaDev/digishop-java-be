package pl.networkmanager.bilka.product.domen.productcrud;

import pl.networkmanager.bilka.product.domen.categorycrud.Category;
import pl.networkmanager.bilka.product.domen.categorycrud.dto.CategoryDto;
import pl.networkmanager.bilka.product.domen.productcrud.dto.CreateProductDto;
import pl.networkmanager.bilka.product.domen.productcrud.dto.ProductDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductMapper {
    static public Product mapFromCreateProductDtoToProduct(CreateProductDto createProductDto, Category category) {
        Product product = new Product();
        product.setCategory(category);
        product.setName(createProductDto.name());
        product.setMainDesc(createProductDto.mainDesc());
        product.setDescHtml(createProductDto.descHtml());
        product.setPrice(createProductDto.price());
        product.setImageUrls(
                createProductDto.imageUrls()
        );
        return product;
    }

    static public ProductDto mapFromProductToProductDto(Product product) {
        List<String> images = new ArrayList<>();
        if (product.getImageUrls() != null) {
            images.addAll(Arrays.stream(product.getImageUrls()).toList());
        }

        CategoryDto categoryDto = CategoryDto.builder()
                .shortId(product.getCategory().getShortId())
                .name(product.getCategory().getName())
                .build();

        return ProductDto.builder()
                .uuid(product.getUuid())
                .createdAt(product.getCreatedAt())
                .isActive(product.isActivate())
                .name(product.getName())
                .mainDesc(product.getMainDesc())
                .descHtml(product.getDescHtml())
                .price(product.getPrice())
                .imageUrls(images)
                .category(categoryDto)
                .build();
    }
}
