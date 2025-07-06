package com.kozak.mybookshop.util;

import com.kozak.mybookshop.dto.category.CategoryRequestDto;
import com.kozak.mybookshop.dto.category.CategoryResponseDto;
import com.kozak.mybookshop.model.Category;

public class CategoryDataTest {
    public static Category sampleCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("roman");
        category.setDescription("about love people");
        return category;
    }

    public static CategoryResponseDto sampleCategoryResponseDto() {
        return new CategoryResponseDto(1L,"roman", "about love people");
    }

    public static CategoryRequestDto sampleCategoryRequestDto() {
        return new CategoryRequestDto()
                .setName("roman")
                .setDescription("about love people");
    }
}
