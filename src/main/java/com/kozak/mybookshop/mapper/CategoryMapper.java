package com.kozak.mybookshop.mapper;

import com.kozak.mybookshop.config.MapperConfig;
import com.kozak.mybookshop.dto.category.CategoryRequestDto;
import com.kozak.mybookshop.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryRequestDto toDto(Category category);

    Category toEntity(CategoryRequestDto dto);

    void updateCategory(@MappingTarget Category category, CategoryRequestDto dto);
}
