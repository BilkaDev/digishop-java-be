package pl.networkmanager.bilka.product.domen.productcrud;

import org.junit.jupiter.api.Test;
import pl.networkmanager.bilka.product.domen.categorycrud.Category;
import pl.networkmanager.bilka.product.domen.categorycrud.CategoryCrudFacade;
import pl.networkmanager.bilka.product.domen.common.exception.ObjectExistInDBException;
import pl.networkmanager.bilka.product.domen.productcrud.dto.CreateProductDto;
import pl.networkmanager.bilka.product.domen.productcrud.dto.ProductDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProductCrudFacadeTest {

    private final ProductRepository productRepository = new ProductRepositoryImplTest();
    private final CategoryCrudFacade categoryCrudFacade = mock(CategoryCrudFacade.class);
    private final QueryManager queryManager = mock(QueryManager.class);
    private final IImageClientFtp imageServerFtp = mock(IImageClientFtp.class);
    private final ProductCrudFacade productCrudFacade = new ProductCrudConfiguration()
            .createForTest(productRepository, categoryCrudFacade, queryManager, imageServerFtp);

    @Test
    public void should_save_product_and_activated_all_images() {
        // given
        CreateProductDto product = CreateProductDto.builder()
                .name("simple product")
                .descHtml("simple product description")
                .mainDesc("simple product description")
                .imageUrls(new String[]{"uuid1111", "uuid2222"})
                .categoryShortId("shortId")
                .build();
        when(categoryCrudFacade.findCategoryByShortId(anyString())).thenReturn(java.util.Optional.of(new Category(1L, "name")));

        // when
        productCrudFacade.createProduct(product);

        // then
        assertThat(productRepository.findAll()).hasSize(1);
        verify(imageServerFtp, times(1)).activeImage("uuid1111");
        verify(imageServerFtp, times(1)).activeImage("uuid2222");
    }

    @Test
    public void should_not_save_product_when_category_not_exist() {
        // given
        CreateProductDto product = CreateProductDto.builder()
                .name("simple product")
                .descHtml("simple product description")
                .mainDesc("simple product description")
                .imageUrls(new String[]{"uuid1111", "uuid2222"})
                .categoryShortId("dont-exists")
                .build();
        when(categoryCrudFacade.findCategoryByShortId("dont-exists")).thenReturn(java.util.Optional.empty());

        // when then
        assertThrows(ObjectExistInDBException.class, () -> productCrudFacade.createProduct(product));
        verify(imageServerFtp, times(0)).activeImage("uuid1111");
        verify(imageServerFtp, times(0)).activeImage("uuid2222");
    }

    @Test
    public void should_disable_product_when_delete_delete_all_images() {
        // given
        CreateProductDto product = CreateProductDto.builder()
                .name("simple product")
                .descHtml("simple product description")
                .mainDesc("simple product description")
                .imageUrls(new String[]{"uuid1111", "uuid2222"})
                .categoryShortId("shortId")
                .build();
        when(categoryCrudFacade.findCategoryByShortId(anyString())).thenReturn(java.util.Optional.of(new Category(1L, "name")));
        productCrudFacade.createProduct(product);

        // when
        productCrudFacade.deleteProduct(productRepository.findAll().getFirst().getUuid());

        // then
        assertThat(productRepository.findAll().getFirst().isActivate()).isFalse();
        verify(imageServerFtp, times(1)).deleteImage("uuid1111");
        verify(imageServerFtp, times(1)).deleteImage("uuid2222");
    }

    @Test
    void should_get_products() {
        String name = "testProduct";
        Category category = Category.builder()
                .id(1L)
                .name("testCategory")
                .build();
        Float priceMin = 10.0f;
        Float priceMax = 100.0f;
        Integer page = 1;
        Integer limit = 10;
        String sort = "name";
        String order = "asc";

        List<Product> mockProductList = new ArrayList<>();
        Product product = new Product();
        product.setName("testProduct");
        product.setCategory(category);
        product.setDescHtml("testProduct description");
        product.setMainDesc("testProduct description");
        product.setPrice(new BigDecimal("10.0"));
        product.setActivate(true);

        mockProductList.add(product);

        when(queryManager.getProduct(anyString(), anyString(), anyFloat(), anyFloat(), anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(mockProductList);

        List<ProductDto> result = productCrudFacade
                .getProduct(name, category.getName(), priceMin, priceMax, page, limit, sort, order);

        assertNotNull(result);
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().createdAt()).isNotNull();
//        assertThat(result.getFirst().isActive()).isTrue();
        verify(queryManager, times(1)).getProduct(name, category.getName(), priceMin, priceMax, page, limit, sort, order);

    }

    @Test
    public void should_throw_exception_when_product_not_exist() {
        // given
        String uid = "not-exist";

        // when then
        assertThrows(ObjectExistInDBException.class, () -> productCrudFacade.getProductByUid(uid));
    }

    @Test
    public void should_return_product_when_product_exist() {
        // given
        CreateProductDto product = CreateProductDto.builder()
                .name("simple product")
                .descHtml("simple product description")
                .mainDesc("simple product description")
                .imageUrls(new String[]{"uuid1111", "uuid2222"})
                .categoryShortId("shortId")
                .build();

        when(categoryCrudFacade.findCategoryByShortId(anyString())).thenReturn(java.util.Optional.of(new Category(1L, "name")));
        productCrudFacade.createProduct(product);

        // when
        ProductDto productByUid = productCrudFacade.getProductByUid(productRepository.findAll().getFirst().getUuid());

        // then
        assertNotNull(productByUid);
    }
}