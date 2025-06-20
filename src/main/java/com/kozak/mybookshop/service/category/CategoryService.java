package com.kozak.mybookshop.service.category;

import com.kozak.mybookshop.dto.category.CategoryRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryRequestDto> findAll(Pageable pageable);

    CategoryRequestDto getById(Long id);

    CategoryRequestDto save(CategoryRequestDto categoryDto);

    CategoryRequestDto update(Long id, CategoryRequestDto categoryDto);

    void deleteById(Long id);
}
