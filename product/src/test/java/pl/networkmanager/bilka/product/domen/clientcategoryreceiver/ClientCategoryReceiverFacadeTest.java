package pl.networkmanager.bilka.product.domen.clientcategoryreceiver;

import org.junit.jupiter.api.Test;
import pl.networkmanager.bilka.product.domen.clientcategoryreceiver.dto.CategoryDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ClientCategoryReceiverFacadeTest {

    private final CategoryRepositoryImpl categoryRepository = new CategoryRepositoryImpl();
    private final ClientCategoryReceiverFacade clientCategoryReceiverFacade = new ClientCategoryReceiverFacade(categoryRepository);

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
        var category = Category.builder()
                .id(1L)
                .uuid("111111111111")
                .name("system")
                .build();
        categoryRepository.setCategories(List.of(category));


        // when
        var categories = clientCategoryReceiverFacade.getCategories();

        // then
        assertThat(categories).hasSize(1);
        assertThat(categories.getFirst().uuid()).isEqualTo("111111111111");
        assertThat(categories.getFirst().name()).isEqualTo("system");
    }

    @Test
    public void should_return_array_with_multiple_elements_when_in_database_there_are_multiple_categories() {
        // given
        var category1 = Category.builder()
                .id(1L)
                .uuid("111111111111")
                .name("system")
                .build();
        var category2 = Category.builder()
                .id(2L)
                .uuid("222222222222")
                .name("game")
                .build();
        categoryRepository.setCategories(List.of(category1, category2));

        // when
        List<CategoryDto> categoryDtos = clientCategoryReceiverFacade.getCategories();

        // then
        assertThat(categoryDtos).hasSize(2);
        assertThat(categoryDtos.get(0).uuid()).isEqualTo("111111111111");
        assertThat(categoryDtos.get(0).name()).isEqualTo("system");
        assertThat(categoryDtos.get(1).uuid()).isEqualTo("222222222222");
        assertThat(categoryDtos.get(1).name()).isEqualTo("game");
    }
}