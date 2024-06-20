package pl.networkmanager.bilka.product.domen.productcrud;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import pl.networkmanager.bilka.product.domen.categorycrud.CategoryCrudFacade;
import pl.networkmanager.bilka.product.domen.categorycrud.Category;
import pl.networkmanager.bilka.product.domen.productcrud.dto.CreateProductDto;
import pl.networkmanager.bilka.product.domen.common.exception.ObjectExistInDBException;

@AllArgsConstructor
public class ProductCrudFacade {
    private final ProductRepository productRepository;
    private final CategoryCrudFacade categoryCrudFacade;

    @Transactional
    public void createProduct(CreateProductDto product) {
        final Category category = categoryCrudFacade.findCategoryByShortId(product.categoryShortId()).orElseThrow(
                () -> new ObjectExistInDBException("Category dont exist in database")
        );
        // @todo active image in file-server activeImage()
        final Product newProduct = ProductMapper.mapFromCreateProductDtoToProduct(product, category);
        newProduct.setActivate(true);
        productRepository.save(newProduct);
    }

    @Transactional
    public void deleteProduct(String uid) {
        productRepository.findProductByUid(uid).ifPresentOrElse(value -> {
            value.setActivate(false);
            productRepository.save(value);
            for (String productImage : value.getImageUrls()) {
                //@todo delete image from file-server
            }
        }, () -> {
            throw new RuntimeException();
        });
    }
}