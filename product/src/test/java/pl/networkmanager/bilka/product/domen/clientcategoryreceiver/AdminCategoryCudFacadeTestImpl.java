package pl.networkmanager.bilka.product.domen.clientcategoryreceiver;

import pl.networkmanager.bilka.product.domen.admincategorycud.AdminCategoryCudFacade;
import pl.networkmanager.bilka.product.domen.admincategorycud.CategoryRepositoryTestImpl;
import pl.networkmanager.bilka.product.domen.admincategorycud.dto.CategoryCreateDto;
import pl.networkmanager.bilka.product.domen.admincategorycud.dto.CategoryDto;
import pl.networkmanager.bilka.product.domen.utils.ShortId;

import java.util.ArrayList;
import java.util.List;

public class AdminCategoryCudFacadeTestImpl extends AdminCategoryCudFacade {
    List<CategoryDto> categoryDtos = new ArrayList<>();

    public AdminCategoryCudFacadeTestImpl() {
        super(new CategoryRepositoryTestImpl());
    }

    @Override
    public void create(CategoryCreateDto categoryCreateDto) {
        CategoryDto categoryDto = CategoryDto
                .builder().shortId(ShortId.generate())
                .name(categoryCreateDto.name()).build();
        categoryDtos.add(categoryDto);
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryDtos;
    }
}
