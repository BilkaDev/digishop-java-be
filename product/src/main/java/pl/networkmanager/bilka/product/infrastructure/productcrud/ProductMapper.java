package pl.networkmanager.bilka.product.infrastructure.productcrud;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.networkmanager.bilka.product.domen.productcrud.dto.CreateProductDto;
import pl.networkmanager.bilka.product.domen.productcrud.dto.ProductDto;
import pl.networkmanager.bilka.product.infrastructure.productcrud.dto.CreateProductRequestDto;
import pl.networkmanager.bilka.product.infrastructure.productcrud.dto.ProductResponseDto;

import java.util.List;

@Component
class ProductMapper {
    @Value("${file-server.url}")
    private String fileServerUrlInstance;

    private static String FILE_SERVER_URL;

    @PostConstruct
    public void init() {
        FILE_SERVER_URL = fileServerUrlInstance;
    }

    public static ProductResponseDto mapFromProductDtoToProductResponseDto(ProductDto product) {
        return ProductResponseDto.builder()
                .uuid(product.uuid())
                .createdAt(product.createdAt())
                .name(product.name())
                .category(product.category())
                .isActive(product.isActive())
                .imageUrls(getImageUrls(product.imageUrls()))
                .mainDesc(product.mainDesc())
                .descHtml(product.descHtml())
                .price(product.price())
                .build();
    }

    public static CreateProductDto mapFromCreateProductRequestDtoToCreateProductDto(
            CreateProductRequestDto createProductRequestDto) {
        return CreateProductDto
                .builder()
                .name(createProductRequestDto.name())
                .categoryShortId(createProductRequestDto.categoryShortId())
                .mainDesc(createProductRequestDto.mainDesc())
                .price(createProductRequestDto.price())
                .descHtml(createProductRequestDto.descHtml())
                .imageUrls(createProductRequestDto.imageUrls())
                .build();
    }

    private static List<String> getImageUrls(List<String> images) {
        return images.stream().map(
                u -> FILE_SERVER_URL + "?uuid=" + u
        ).toList();
    }
}
