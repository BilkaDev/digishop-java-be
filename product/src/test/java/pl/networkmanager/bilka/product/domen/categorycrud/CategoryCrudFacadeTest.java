package pl.networkmanager.bilka.product.domen.categorycrud;

import org.junit.jupiter.api.Test;
import pl.networkmanager.bilka.product.domen.categorycrud.dto.CategoryCreateDto;
import pl.networkmanager.bilka.product.domen.categorycrud.dto.CategoryDto;
import pl.networkmanager.bilka.product.domen.common.exception.ObjectExistInDBException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CategoryCrudFacadeTest {

    private final CategoryRepository categoryRepository = new CategoryRepositoryTestImpl();
    CategoryCrudFacade categoryCrudFacade = new CategoryCrudConfiguration()
            .categoryCrudFacade(categoryRepository);

    @Test
    public void should_add_new_category_to_db_and_generate_new_uuid() {
        // gave
        CategoryCreateDto category = CategoryCreateDto.builder()
                .name("system")
                .build();
        // when
        categoryCrudFacade.create(category);

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
        categoryCrudFacade.create(category);
        assertThatThrownBy(() -> categoryCrudFacade.create(category)).isInstanceOf(ObjectExistInDBException.class);

    }

    @Test
    public void should_return_empty_array_when_db_is_empty() {
        // given and when
        var categories = categoryCrudFacade.getCategories();
        // then
        assertThat(categories).isEmpty();
    }

    @Test
    public void should_return_array_with_one_element_when_in_database_there_is_one_category() {
        // given
        categoryCrudFacade.create(CategoryCreateDto.builder().name("system").build());

        // when
        var categories = categoryCrudFacade.getCategories();

        // then
        assertThat(categories).hasSize(1);
        assertThat(categories.getFirst().shortId()).isNotNull().hasSize(12);
        assertThat(categories.getFirst().name()).isEqualTo("system");
    }

    @Test
    public void should_return_optional_empty_when_find_category_with_dont_exist_short_id() {
        // given when
        var category = categoryCrudFacade.findCategoryByShortId("dontexist").orElse(null);

        // then
        assertThat(category).isNull();
    }

    @Test
    public void should_return_category_by_short_id() {
        // given && when
        categoryCrudFacade.create(CategoryCreateDto.builder().name("exist-short-id").build());
        var category = categoryCrudFacade.getCategories().getFirst();
        // then
        var categoryByShortId = categoryCrudFacade.findCategoryByShortId(category.shortId()).orElse(null);
        assertThat(categoryByShortId).isNotNull();
        assertThat(categoryByShortId.name()).isEqualTo("exist-short-id");
    }

    @Test
    public void should_return_empty_array_when_in_database_there_are_no_categories() {
        // given & when
        var categories = categoryCrudFacade.getCategories();

        // then
        assertThat(categories).isEmpty();
    }


    @Test
    public void should_return_array_with_multiple_elements_when_in_database_there_are_multiple_categories() {
//        // given
        categoryCrudFacade.create(CategoryCreateDto.builder().name("system").build());
        categoryCrudFacade.create(CategoryCreateDto.builder().name("game").build());
        // when
        List<CategoryDto> categoryDtos = categoryCrudFacade.getCategories();

        // then
        assertThat(categoryDtos).hasSize(2);
        assertThat(categoryDtos.get(0).shortId()).isNotNull().hasSize(12);
        assertThat(categoryDtos.get(0).name()).isEqualTo("system");
        assertThat(categoryDtos.get(1).shortId()).isNotNull().hasSize(12);
        assertThat(categoryDtos.get(1).name()).isEqualTo("game");
    }
}