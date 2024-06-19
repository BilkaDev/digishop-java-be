package pl.networkmanager.bilka.product.domen.clientcategoryreceiver;

import pl.networkmanager.bilka.product.domen.admincategorycud.AdminCategoryCudFacade;
import pl.networkmanager.bilka.product.domen.admincategorycud.CategoryRepositoryImpl;
import pl.networkmanager.bilka.product.domen.admincategorycud.dto.CategoryCreateDto;
import pl.networkmanager.bilka.product.domen.admincategorycud.dto.CategoryDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminCategoryCudFacadeImpl extends AdminCategoryCudFacade {
    List<CategoryDto> categoryDtos = new ArrayList<>();

    public AdminCategoryCudFacadeImpl() {
        super(new CategoryRepositoryImpl());
    }

    @Override
    public void create(CategoryCreateDto categoryCreateDto) {
        CategoryDto categoryDto = CategoryDto
                .builder().shortId(UUID.randomUUID().toString().replace("-", "").substring(0, 12))
                .name(categoryCreateDto.name()).build();
        categoryDtos.add(categoryDto);
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryDtos;
    }
}
