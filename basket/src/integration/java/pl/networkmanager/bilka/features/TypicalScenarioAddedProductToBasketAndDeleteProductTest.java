package pl.networkmanager.bilka.features;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;
import pl.networkmanager.bilka.BaseIntegrationTest;
import pl.networkmanager.bilka.basket.domain.basketcrud.IProductRestTemplate;
import pl.networkmanager.bilka.basket.domain.basketcrud.entity.Product;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TypicalScenarioAddedProductToBasketAndDeleteProductTest extends BaseIntegrationTest {
    @MockBean
    private IProductRestTemplate productRestTemplate;

    @Test
    public void happy_path_test() throws Exception {
        // 1. Customer add product to basket and system create new basket and add product to it and should
        // return basket uuid in cookie and header X-Total-Count should be 1
        //given
        var product1 = new Product();
        product1.setUuid("1");
        product1.setImageUrls(new String[]{"url"});
        product1.setPrice(BigDecimal.valueOf(10));
        when(productRestTemplate.getProduct("1")).thenReturn(
                product1
        );
        ResultActions addProductActionResult = mockMvc.perform(
                post("/api/v1/basket")
                        .content("{\"product\": 1, \"quantity\": 1}")
                        .contentType("application/json"));

        //then
        addProductActionResult
                .andExpect(status().isOk())
                .andExpect(cookie().exists("basket"))
                .andExpect(header().string("X-Total-Count", "1"));
        verify(productRestTemplate).getProduct("1");

        // 2. Should add product to existing basket when customer add product to basket and
        // system should return ok and header X-Total-Count should be 2

        // given
        var product2 = new Product();
        product2.setUuid("2");
        product2.setImageUrls(new String[]{"url"});
        product2.setPrice(BigDecimal.valueOf(20));
        Cookie cookie = addProductActionResult.andReturn().getResponse().getCookie("basket");
        when(productRestTemplate.getProduct("2")).thenReturn(
                product2
        );
        // when
        ResultActions addSecondProductActionResult = mockMvc.perform(
                post("/api/v1/basket")
                        .content("{\"product\": 2, \"quantity\": 3}")
                        .cookie(cookie)
                        .contentType("application/json"));

        // then
        addSecondProductActionResult
                .andExpect(status().isOk())
                .andExpect(header().string("X-Total-Count", "4"));
        // 3. Customer get basket and system should return basket with products and header X-Total-Count should be 2
        // and total price x
        // when
        ResultActions getBasketActionResult = mockMvc.perform(
                get("/api/v1/basket")
                        .cookie(cookie)
                        .contentType("application/json"));

        // then

        getBasketActionResult
                .andExpect(status().isOk())
                .andExpect(header().string("X-Total-Count", "4"))
                .andExpect(jsonPath("$.totalPrice").value("70"));

        // 4. Customer delete product from basket and system delete product from basket
        // and should return ok and header X-Total-Count should be 1
        // when
        ResultActions deleteProductActionResult = mockMvc.perform(
                delete("/api/v1/basket?uuid=1")
                        .cookie(cookie)
                        .contentType("application/json"));
        // then
        deleteProductActionResult
                .andExpect(status().isOk())
                .andExpect(header().string("X-Total-Count", "3"));
        // 5. Customer delete all products from basket and system should deleted basket
        // when
        mockMvc.perform(
                delete("/api/v1/basket?uuid=2")
                        .cookie(cookie)
                        .contentType("application/json"));

        ResultActions getEmptyBasketActionResult = mockMvc.perform(
                get("/api/v1/basket")
                        .cookie(cookie)
                        .contentType("application/json"));
        // then
        getEmptyBasketActionResult
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("NoBasketInfoException"));
    }
}
