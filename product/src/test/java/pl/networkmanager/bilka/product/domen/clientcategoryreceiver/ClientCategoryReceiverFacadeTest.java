package pl.networkmanager.bilka.product.domen.clientcategoryreceiver;

import org.junit.jupiter.api.Test;
import pl.networkmanager.bilka.product.domen.admincategorycud.AdminCategoryCudFacade;
import pl.networkmanager.bilka.product.domen.admincategorycud.dto.CategoryCreateDto;
import pl.networkmanager.bilka.product.domen.clientcategoryreceiver.dto.CategoryDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ClientCategoryReceiverFacadeTest {

    private final AdminCategoryCudFacade adminCategoryCudFacade = new AdminCategoryCudFacadeTestImpl();
    private final ClientCategoryReceiverFacade clientCategoryReceiverFacade = new ClientCategoryReceiverConfiguration()
            .clientCategoryReceiverFacade(adminCategoryCudFacade);

    @Test
    public void should_return_empty_array_when_in_database_there_are_no_categories() {
        // given & when
        var categories = clientCategoryReceiverFacade.getCategories();

        // then
        assertThat(categories).isEmpty();
    }

    @Test
    public void should_return_array_with_one_element_when_in_database_there_is_one_category() {
        // given
        adminCategoryCudFacade.create(CategoryCreateDto.builder().name("system").build());

        // when
        var categories = clientCategoryReceiverFacade.getCategories();

        // then
        assertThat(categories).hasSize(1);
        assertThat(categories.getFirst().shortId()).isNotNull().hasSize(12);
        assertThat(categories.getFirst().name()).isEqualTo("system");
    }

    @Test
    public void should_return_array_with_multiple_elements_when_in_database_there_are_multiple_categories() {
//        // given
        adminCategoryCudFacade.create(CategoryCreateDto.builder().name("system").build());
        adminCategoryCudFacade.create(CategoryCreateDto.builder().name("game").build());
        // when
        List<CategoryDto> categoryDtos = clientCategoryReceiverFacade.getCategories();

        // then
        assertThat(categoryDtos).hasSize(2);
        assertThat(categoryDtos.get(0).shortId()).isNotNull().hasSize(12);
        assertThat(categoryDtos.get(0).name()).isEqualTo("system");
        assertThat(categoryDtos.get(1).shortId()).isNotNull().hasSize(12);
        assertThat(categoryDtos.get(1).name()).isEqualTo("game");
    }
}