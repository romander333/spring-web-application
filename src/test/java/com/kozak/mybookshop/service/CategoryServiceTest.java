package com.kozak.mybookshop.service;

import static com.kozak.mybookshop.util.CategoryDataTest.sampleCategory;
import static com.kozak.mybookshop.util.CategoryDataTest.sampleCategoryRequestDto;
import static com.kozak.mybookshop.util.CategoryDataTest.sampleCategoryResponseDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kozak.mybookshop.dto.category.CategoryRequestDto;
import com.kozak.mybookshop.dto.category.CategoryResponseDto;
import com.kozak.mybookshop.exception.EntityNotFoundException;
import com.kozak.mybookshop.mapper.CategoryMapper;
import com.kozak.mybookshop.model.Category;
import com.kozak.mybookshop.repository.category.CategoryRepository;
import com.kozak.mybookshop.service.category.CategoryServiceImpl;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("find all categories")
    void findAll_WithValidPageable_ShouldReturnPageOfCategoryDtos() {
        Category category = sampleCategory();
        Category category2 = new Category();
        category.setId(2L);
        category.setName("fantastic");
        category.setDescription("about not real situation");

        CategoryResponseDto dto1 = sampleCategoryResponseDto();
        CategoryResponseDto dto2 =
                new CategoryResponseDto(2L,"fantastic", "about not real situation");

        Page<Category> categoryPage = new PageImpl<>(Arrays.asList(category, category2));
        Pageable pageable = PageRequest.of(0, 10);

        when(categoryMapper.toDto(category)).thenReturn(dto1);
        when(categoryMapper.toDto(category2)).thenReturn(dto2);
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);

        Page<CategoryResponseDto> actual = categoryService.findAll(pageable);
        assertEquals(2, actual.getContent().size());
        assertEquals(dto1.name(), actual.getContent().get(0).name());
        assertEquals(dto2.name(), actual.getContent().get(1).name());
    }

    @Test
    @DisplayName("save category when valid category provided")
    void save_WithValidCategory_ReturnCategoryDto() {
        Category category = sampleCategory();
        Category savedCategory = sampleCategory();

        CategoryRequestDto requestDto = sampleCategoryRequestDto();
        CategoryResponseDto dto = sampleCategoryResponseDto();

        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryMapper.toDto(savedCategory)).thenReturn(dto);
        when(categoryRepository.save(category)).thenReturn(savedCategory);

        CategoryResponseDto actual = categoryService.save(requestDto);
        verify(categoryRepository).save(category);
        assertNotNull(actual);
        assertEquals(actual.name(), category.getName());
        assertEquals(actual.id(), savedCategory.getId());
        assertEquals(actual.description(), savedCategory.getDescription());
    }

    @Test
    @DisplayName("get category when valid category id provided")
    void getById_WithValidCategoryId_ShouldReturnCategoryDto() {
        Long categoryId = 1L;
        Category category = sampleCategory();

        CategoryResponseDto dto = sampleCategoryResponseDto();

        when(categoryMapper.toDto(category)).thenReturn(dto);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        CategoryResponseDto actual = categoryService.getById(categoryId);
        verify(categoryRepository).findById(categoryId);
        assertEquals(actual.name(), category.getName());
        assertEquals(actual.id(), category.getId());
        assertEquals(actual.description(), category.getDescription());
    }

    @Test
    @DisplayName("get category by invalid id and expected exception")
    void getById_WithInValidCategoryId_ShouldThrownEntityNotFoundException() {
        Long categoryId = -100L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getById(categoryId);
        });

        String actual = exception.getMessage();
        assertEquals("Cannot find Category with id: " + categoryId, actual);
    }

    @Test
    @DisplayName("delete category when valid category id provided")
    void deleteById_WithValidId_ShouldDeleteCategoryDto() {
        Long categoryId = 1L;
        categoryService.deleteById(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    @DisplayName("update category when invalid category id provided")
    void update_WithInValidId_ShouldThrowEntityNotFoundException() {
        Long categoryId = -100L;
        CategoryRequestDto requestDto = sampleCategoryRequestDto();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            categoryService.update(categoryId,requestDto);
        });

        String actual = exception.getMessage();
        assertEquals("Cannot find Category with id: " + categoryId, actual);
    }
}
