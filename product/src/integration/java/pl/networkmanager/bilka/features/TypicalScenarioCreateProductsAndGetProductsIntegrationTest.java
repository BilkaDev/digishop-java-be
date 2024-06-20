package pl.networkmanager.bilka.features;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import pl.networkmanager.bilka.BaseIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TypicalScenarioCreateProductsAndGetProductsIntegrationTest extends BaseIntegrationTest {

    @Test
    public void test1() throws Exception {
        //0. Customer did get/category and system replied 200 ok and 0 category.
        ResultActions resultActions = mockMvc
                .perform(
                        get("/api/v1/category").contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk());

        //1. Customer did GET/product page1 limit 10 and system replied 200 ok and 0 products.
        //given
        //when
//        ResultActions resultActions = mockMvc
//                .perform(
//                        get("/product?page=1&limit=10").contentType(MediaType.APPLICATION_JSON));
//        //then
//        resultActions.andExpect(status().isOk());

        //2. customer did GET/category and system replied 200 ok and 0 category.
        //3. admin did POST/product and system replied 400 categories don't exist.
        //4. admin did POST/category and system replied 201 ok non-content x2
        //5. admin did a POST/product and the system replied 201 ok non-content x14
        //6. customer did GET/product page2 limit 10 and system replied 200 ok and 4 products.
        //7. customer did GET/category and system replied 200 ok and 2 category.
        //8. the customer did GET/product page0 limit 10 category 1 and the system replied 200 ok and X products.
        //9. customer did GET/product/uuid and the system returned 200 ok and 1 product.
        //10. admin did DELETE/product/uuid and the system replied 204 ok.
        //11. customer did GET/product/uuid and the system replied 404 not found.
    }

}
