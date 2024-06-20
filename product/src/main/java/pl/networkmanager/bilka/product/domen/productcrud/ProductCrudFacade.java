package pl.networkmanager.bilka.product.domen.productcrud;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import pl.networkmanager.bilka.product.domen.categorycrud.Category;
import pl.networkmanager.bilka.product.domen.categorycrud.CategoryCrudFacade;
import pl.networkmanager.bilka.product.domen.common.exception.ObjectExistInDBException;
import pl.networkmanager.bilka.product.domen.productcrud.dto.CreateProductDto;
import pl.networkmanager.bilka.product.domen.productcrud.dto.ProductDto;

import java.util.List;

@AllArgsConstructor
public class ProductCrudFacade {
    private final ProductRepository productRepository;
    private final CategoryCrudFacade categoryCrudFacade;
    private final QueryManager queryManager;
    private final IImageClientFtp imageServerFtp;

    @Transactional
    public void createProduct(CreateProductDto product) {
        final Category category = categoryCrudFacade.findCategoryByShortId(product.categoryShortId()).orElseThrow(
                () -> new ObjectExistInDBException("Category dont exist in database")
        );
        for (String uuid : product.imageUrls()) {
            imageServerFtp.activeImage(uuid);
        }
        final Product newProduct = ProductMapper.mapFromCreateProductDtoToProduct(product, category);
        newProduct.setActivate(true);
        productRepository.save(newProduct);
    }

    @Transactional
    public void deleteProduct(String uid) {
        productRepository.findProductByUid(uid).ifPresentOrElse(value -> {
            value.setActivate(false);
            productRepository.save(value);
            for (String uuid : value.getImageUrls()) {
                imageServerFtp.deleteImage(uuid);
            }
        }, () -> {
            throw new RuntimeException();
        });
    }

    public List<ProductDto> getProduct(
            String name,
            String category,
            Float price_min,
            Float price_max,
            Integer page,
            Integer limit,
            String sort,
            String order
    ) {
        List<Product> product = queryManager.getProduct(name, category, price_min, price_max, page, limit, sort, order);

        return product.stream().map(ProductMapper::mapFromProductToProductDto).toList();
    }

    public ProductDto getProductByUid(String uid) {
        return productRepository.findProductByUid(uid)
                .map(ProductMapper::mapFromProductToProductDto)
                .orElseThrow(() -> new ObjectExistInDBException("Product dont exist in database"));
    }

}