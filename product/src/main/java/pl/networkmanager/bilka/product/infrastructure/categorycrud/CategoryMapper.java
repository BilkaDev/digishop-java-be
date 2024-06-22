package pl.networkmanager.bilka.product.infrastructure.categorycrud;

import pl.networkmanager.bilka.product.domen.categorycrud.dto.CategoryCreateDto;
import pl.networkmanager.bilka.product.domen.categorycrud.dto.CategoryDto;
import pl.networkmanager.bilka.product.infrastructure.categorycrud.dto.CategoryCreateRequestDto;
import pl.networkmanager.bilka.product.infrastructure.categorycrud.dto.CategoryResponseDto;

class CategoryMapper {
    public static CategoryCreateDto mapFromCategoryCreateRequestDtoToCategoryCreateDto(
            CategoryCreateRequestDto categoryCreateRequestDto) {
        return CategoryCreateDto.builder()
                .name(categoryCreateRequestDto.name())
                .build();
    }


    public static CategoryResponseDto mapFromCategoryToCategoryResponseDto(CategoryDto category) {
        return CategoryResponseDto.builder()
                .name(category.name())
                .shortId(category.shortId())
                .build();
    }
}
