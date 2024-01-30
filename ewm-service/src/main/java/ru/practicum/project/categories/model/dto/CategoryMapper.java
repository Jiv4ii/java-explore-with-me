package ru.practicum.project.categories.model.dto;


import ru.practicum.project.categories.model.Category;
import ru.practicum.project.categories.model.dto.CategoryDto;

public final class CategoryMapper {

    private CategoryMapper() {
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }


    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
