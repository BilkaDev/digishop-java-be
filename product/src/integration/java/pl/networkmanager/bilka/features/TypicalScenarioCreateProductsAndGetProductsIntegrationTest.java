package pl.networkmanager.bilka.features;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import pl.networkmanager.bilka.BaseIntegrationTest;
import pl.networkmanager.bilka.product.domen.productcrud.IImageClientFtp;
import pl.networkmanager.bilka.product.infrastructure.categorycrud.dto.CategoryCreateRequestDto;
import pl.networkmanager.bilka.product.infrastructure.categorycrud.dto.CategoryResponseDto;
import pl.networkmanager.bilka.product.infrastructure.productcrud.dto.CreateProductRequestDto;
import pl.networkmanager.bilka.product.infrastructure.productcrud.dto.ProductResponseDto;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TypicalScenarioCreateProductsAndGetProductsIntegrationTest extends BaseIntegrationTest {
    @Value("${file-server.url}")
    private String fileServerUrl;

    @MockBean
    private IImageClientFtp imageServerFtp;

    @Test
    public void test1() throws Exception {
        //1. Customer did get/category and system replied 200 ok and 0 category.
        ResultActions resultActions = mockMvc.perform(get("/api/v1/category").contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk()).andExpect(content().json("[]"));

        //2. Customer did GET/product and system replied 200 ok and 0 products.
        //given
        //when
        ResultActions getEmptyProductActions = mockMvc.perform(get("/api/v1/product").contentType(MediaType.APPLICATION_JSON));
        //then
        getEmptyProductActions
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        //3. admin did POST/product and system replied 400 categories don't exist.
        // given
        CreateProductRequestDto product = new CreateProductRequestDto(
                "name",
                "system",
                "mainDesc",
                new BigDecimal("1.0"),
                "descHtml",
                new String[]{"uuid-img"}
        );

        // when
        ResultActions postProductActions = mockMvc.perform(post("/api/v1/product")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(product)));
        // then
        postProductActions
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"messages\":[\"Category dont exist in database\"]}"));

        //4. admin did POST/category and system replied 201 ok non-content x2
        // given
        CategoryCreateRequestDto category1 = new CategoryCreateRequestDto("system");
        CategoryCreateRequestDto category2 = new CategoryCreateRequestDto("game");
        // when then
        mockMvc.perform(post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(category1)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(category2)))
                .andExpect(status().isCreated());
        //4.1 admin did Post/category with exist name and system replied 400 category already exist.
        mockMvc.perform(post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(category1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"messages\":[\"Category exists in database with this name\"]}"));
        //5 Customer did get/category and system replied 200 ok and 2 category.
        ResultActions getCategoriesActions = mockMvc.perform(get("/api/v1/category").contentType(MediaType.APPLICATION_JSON));
        //then
        getCategoriesActions
                .andExpect(status().isOk());
        List<CategoryResponseDto> categories = objectMapper.readValue(
                getCategoriesActions.andReturn().getResponse().getContentAsString(),
                new TypeReference<List<CategoryResponseDto>>() {
                });
        //6. admin did a POST/product and the system replied 201 ok non-content x14
        // given

        for (int i = 0; i < 14; i++) {
            CreateProductRequestDto product1 = new CreateProductRequestDto(
                    "name" + i,
                    categories.get(i % 2).shortId(),
                    "mainDesc" + i,
                    new BigDecimal("1.0"),
                    "descHtml" + i,
                    new String[]{"uuid-img" + i}
            );
            // when
            ResultActions createProduct = mockMvc.perform(post("/api/v1/product")
                            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(product1)))
                    .andExpect(status().isCreated());
            // then
            createProduct.andExpect(status().isCreated());
            verify(imageServerFtp).activeImage("uuid-img" + i);
        }
        //6. customer did GET/product page2 limit 10 and system replied 200 ok and 4 products and return header X-Total-Count=14.
        // given
        // when
        ResultActions getProductsActions = mockMvc.perform(get("/api/v1/product?page=2&limit=10").contentType(MediaType.APPLICATION_JSON));
        var response = getProductsActions.andReturn().getResponse();
        // then
        getProductsActions
                .andExpect(status().isOk());
        List<ProductResponseDto> products = objectMapper.readValue(
                response.getContentAsString(),
                new TypeReference<List<ProductResponseDto>>() {
                });

        assertThat(products).hasSize(4);
        assertThat(response.getHeader("X-Total-Count")).isEqualTo("14");
        assertThat(products.getFirst().createdAt()).isNotNull();
        assertThat(products.getFirst().category().name()).isNotNull();
        assertThat(products.getFirst().isActive()).isEqualTo(true);

        //6.1 customer did GET/product and products should contain url to get image from ftp-server.
        assertThat(products.getFirst().imageUrls()).hasSize(1);
        assertThat(products.getFirst().imageUrls().getFirst())
                .contains(fileServerUrl + "?uuid=" + "uuid-img10");

        //7. customer did GET/category and system replied 200 ok and 2 category.
        // given
        // when
        ResultActions getCategoriesActions2 = mockMvc.perform(get("/api/v1/category").contentType(MediaType.APPLICATION_JSON));
        // then
        getCategoriesActions2
                .andExpect(status().isOk());
        List<CategoryResponseDto> categories2 = objectMapper.readValue(
                getCategoriesActions2.andReturn().getResponse().getContentAsString(),
                new TypeReference<List<CategoryResponseDto>>() {
                });
        assertThat(categories2).hasSize(2);

        //8. the customer did GET/product page0 limit 10 category 1 and the system replied 200 ok and X products.
        // given
        // when
        ResultActions getProductsActions2 = mockMvc.perform(
                get("/api/v1/product?page=0&limit=10&categoryShortId=" + categories2.getFirst().shortId())
                        .contentType(MediaType.APPLICATION_JSON));
        // then
        getProductsActions2
                .andExpect(status().isOk());
        List<ProductResponseDto> products2 = objectMapper.readValue(
                getProductsActions2.andReturn().getResponse().getContentAsString(),
                new TypeReference<List<ProductResponseDto>>() {
                });
        assertThat(products2).hasSize(7);

        //9. customer did GET/product/uuid and the system returned 200 ok and 1 product.
        // given
        // when
        ResultActions getProductActions = mockMvc.perform(get("/api/v1/product/" + products.getFirst().uuid())
                .contentType(MediaType.APPLICATION_JSON));
        // then
        getProductActions
                .andExpect(status().isOk());
        ProductResponseDto productResponse = objectMapper.readValue(
                getProductActions.andReturn().getResponse().getContentAsString(),
                ProductResponseDto.class);
        assertThat(productResponse).isNotNull();
        assertThat(productResponse.uuid()).isEqualTo(products.getFirst().uuid());
        assertThat(productResponse.isActive()).isEqualTo(true);
        //10. admin did DELETE/product/uuid and the system replied 204 ok.
        // given
        // when
        ProductResponseDto productToDelete = products.getFirst();
        mockMvc.perform(delete("/api/v1/product/" + productToDelete.uuid())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(imageServerFtp).deleteImage(anyString());
        //11. customer did GET/product/uuid and the system replied 200 and product is not active.
        ResultActions resultActionGetOneNotActiveProduct = mockMvc.perform(get("/api/v1/product/" + productToDelete.uuid())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        ProductResponseDto productResponseDto = objectMapper.readValue(
                resultActionGetOneNotActiveProduct.andReturn().getResponse().getContentAsString(),
                ProductResponseDto.class);
        assertThat(productResponseDto.isActive()).isFalse();
    }

}
