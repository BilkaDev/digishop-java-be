package pl.networkmanager.bilka.product.domen.productcrud;

import org.junit.jupiter.api.Test;
import pl.networkmanager.bilka.product.domen.categorycrud.CategoryCrudFacade;
import pl.networkmanager.bilka.product.domen.categorycrud.Category;
import pl.networkmanager.bilka.product.domen.productcrud.dto.CreateProductDto;
import pl.networkmanager.bilka.product.domen.common.exception.ObjectExistInDBException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductCrudFacadeTest {

    private final ProductRepository productRepository = new ProductRepositoryImplTest();
    private final CategoryCrudFacade categoryCrudFacade = mock(CategoryCrudFacade.class);
    private final ProductCrudFacade productCrudFacade = new ProductCrudConfiguration()
            .productCrudFacade(productRepository, categoryCrudFacade);


    @Test
    public void should_save_product() {
        // given
        CreateProductDto product = CreateProductDto.builder()
                .name("simple product")
                .descHtml("simple product description")
                .mainDesc("simple product description")
                .imageUrls(new String[]{"url1", "url2"})
                .categoryShortId("shortId")
                .build();
        when(categoryCrudFacade.findCategoryByShortId("shortId")).thenReturn(java.util.Optional.of(new Category(1L, "shortId", "name")));

        // when
        productCrudFacade.createProduct(product);

        // then
        assertThat(productRepository.findAll()).hasSize(1);
    }

    @Test
    public void should_not_save_product_when_category_not_exist() {
        // given
        CreateProductDto product = CreateProductDto.builder()
                .name("simple product")
                .descHtml("simple product description")
                .mainDesc("simple product description")
                .imageUrls(new String[]{"url1", "url2"})
                .categoryShortId("dont-exists")
                .build();
        when(categoryCrudFacade.findCategoryByShortId("dont-exists")).thenReturn(java.util.Optional.empty());

        // when then
        assertThrows(ObjectExistInDBException.class, () -> productCrudFacade.createProduct(product));
    }

    @Test
    public void should_disable_product_when_delete() {
        // given
        CreateProductDto product = CreateProductDto.builder()
                .name("simple product")
                .descHtml("simple product description")
                .mainDesc("simple product description")
                .imageUrls(new String[]{"url1", "url2"})
                .categoryShortId("shortId")
                .build();
        when(categoryCrudFacade.findCategoryByShortId("shortId")).thenReturn(java.util.Optional.of(new Category(1L, "shortId", "name")));
        productCrudFacade.createProduct(product);

        // when
        productCrudFacade.deleteProduct(productRepository.findAll().getFirst().getUid());

        // then
        assertThat(productRepository.findAll().getFirst().isActivate()).isFalse();
    }
}