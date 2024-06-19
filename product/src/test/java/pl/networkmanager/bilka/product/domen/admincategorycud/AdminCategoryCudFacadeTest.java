package pl.networkmanager.bilka.product.domen.admincategorycud;

import org.junit.jupiter.api.Test;
import pl.networkmanager.bilka.product.domen.admincategorycud.dto.CategoryCreateDto;
import pl.networkmanager.bilka.product.domen.admincategorycud.exception.ObjectExistInDBException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AdminCategoryCudFacadeTest {

    private final CategoryRepository categoryRepository = new CategoryRepositoryTestImpl();
    AdminCategoryCudFacade adminCategoryCudFacade = new AdminCategoryCudConfiguration()
            .adminCategoryCudFacade(categoryRepository);

    @Test
    public void should_add_new_category_to_db_and_generate_new_uuid() {
        // gave
        CategoryCreateDto category = CategoryCreateDto.builder()
                .name("system")
                .build();
        // when
        adminCategoryCudFacade.create(category);

        //then
        var categories = categoryRepository.findAll();
        assertThat(categories).hasSize(1);
        assertThat(categories.getFirst().name()).isEqualTo("system");
        assertThat(categories.getFirst().shortId()).hasSize(12);
    }

    @Test
    public void should_throw_runtime_exception_when_name_exist_in_db() {
        // gave
        CategoryCreateDto category = CategoryCreateDto.builder()
                .name("system")
                .build();
        // when && then
        adminCategoryCudFacade.create(category);
        assertThatThrownBy(() -> adminCategoryCudFacade.create(category)).isInstanceOf(ObjectExistInDBException.class);

    }

    @Test
    public void should_return_empty_array_when_db_is_empty() {
        // given and when
        var categories = adminCategoryCudFacade.findAll();
        // then
        assertThat(categories).isEmpty();
    }

    @Test
    public void should_return_array_with_one_element_when_in_database_there_is_one_category() {
        // given
        adminCategoryCudFacade.create(CategoryCreateDto.builder().name("system").build());

        // when
        var categories = adminCategoryCudFacade.findAll();

        // then
        assertThat(categories).hasSize(1);
        assertThat(categories.getFirst().shortId()).isNotNull().hasSize(12);
        assertThat(categories.getFirst().name()).isEqualTo("system");
    }

    @Test
    public void should_return_optional_empty_when_find_category_with_dont_exist_short_id() {
        // given when
        var category = adminCategoryCudFacade.findCategoryByShortId("dontexist").orElse(null);

        // then
        assertThat(category).isNull();
    }

    @Test
    public void should_return_category_by_short_id() {
        // given && when
        adminCategoryCudFacade.create(CategoryCreateDto.builder().name("exist-short-id").build());
        var category = adminCategoryCudFacade.findAll().getFirst();
        // then
        var categoryByShortId = adminCategoryCudFacade.findCategoryByShortId(category.shortId()).orElse(null);
        assertThat(categoryByShortId).isNotNull();
        assertThat(categoryByShortId.name()).isEqualTo("exist-short-id");
    }
}