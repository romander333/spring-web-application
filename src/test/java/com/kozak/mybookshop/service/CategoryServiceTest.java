package com.kozak.mybookshop.service;

import com.kozak.mybookshop.dto.category.CategoryRequestDto;
import com.kozak.mybookshop.dto.category.CategoryResponseDto;
import com.kozak.mybookshop.exception.EntityNotFoundException;
import com.kozak.mybookshop.mapper.CategoryMapper;
import com.kozak.mybookshop.model.Category;
import com.kozak.mybookshop.repository.category.CategoryRepository;
import com.kozak.mybookshop.service.category.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;


    @Test
    void findAll_WithValidPageable_ShouldReturnPageOfCategoryDtos() {
        Category category = new Category();
        category.setName("test");

        Category category2 = new Category();
        category2.setName("test2");

        CategoryResponseDto dto1 = new CategoryResponseDto(1L,"test", "description");
        CategoryResponseDto dto2 = new CategoryResponseDto(2L,"test2", "description2");

        Page<Category> categoryPage = new PageImpl<>(Arrays.asList(category, category2));
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(categoryMapper.toDto(category)).thenReturn(dto1);
        Mockito.when(categoryMapper.toDto(category2)).thenReturn(dto2);
        Mockito.when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);

        Page<CategoryResponseDto> actual = categoryService.findAll(pageable);
        assertEquals(2, actual.getContent().size());
        assertEquals("test", actual.getContent().get(0).name());
    }

    @Test
    void save_WithValidCategory_ReturnCategoryDto() {
        Category category = new Category();
        category.setName("test");
        category.setDescription("description");

        Category savedCategory = new Category();
        category.setId(1L);
        savedCategory.setName("test");
        savedCategory.setDescription("description");

        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("test");

        CategoryResponseDto dto = new CategoryResponseDto(1L,"test", "description");

        Mockito.when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        Mockito.when(categoryMapper.toDto(savedCategory)).thenReturn(dto);
        Mockito.when(categoryRepository.save(category)).thenReturn(savedCategory);

        CategoryResponseDto actual = categoryService.save(requestDto);

        verify(categoryRepository).save(category);
        assertNotNull(actual);
        assertEquals(actual.name(), category.getName());
    }


    @Test
    void getById_WithValidCategoryId_ShouldReturnTrue() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("test");
        category.setDescription("description");

        CategoryResponseDto dto = new CategoryResponseDto(1L,"test", "description");

        Mockito.when(categoryMapper.toDto(category)).thenReturn(dto);
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        CategoryResponseDto actual = categoryService.getById(categoryId);

        verify(categoryRepository).findById(categoryId);
        assertEquals(actual.name(), category.getName());
    }

    @Test
    void getById_WithInValidCategoryId_ShouldThrownEntityNotFound() {
        Long categoryId = -100L;

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getById(categoryId);
        }
        );

        String actual = exception.getMessage();

        assertEquals("Cannot find Category with id: " + categoryId, actual);
    }

    @Test
    void deleteById_WithValidId_ShouldNotReturn() {
        Long categoryId = 1L;

        categoryService.deleteById(categoryId);

        verify(categoryRepository).deleteById(categoryId);
    }

//
//    @Test
//    void update_WithValidData_ShouldReturnResponseDto() {
//        Long categoryId = 1L;
//        Category category = new Category();
//        category.setName("test");
//        category.setDescription("description");
//
//        Category savedCategory = new Category();
//        savedCategory.setId(categoryId);
//        savedCategory.setName("test");
//        savedCategory.setDescription("description");
//
//        CategoryResponseDto dto = new CategoryResponseDto(1L,"test", "description");
//
//        CategoryRequestDto requestDto = new CategoryRequestDto();
//        requestDto.setName("otherTest");
//        requestDto.setDescription("description");
//
//        Mockito.when(categoryMapper.toEntity(requestDto)).thenReturn(category);
//        Mockito.when(categoryMapper.toDto(category)).thenReturn(dto);
//        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
//
//        Mockito.doAnswer(invocation -> {
//            Category cat = invocation.getArgument(0);
//            CategoryRequestDto categoryRequestDto = invocation.getArgument(1);
//
//            cat.setName(categoryRequestDto.getName());
//            cat.setDescription(categoryRequestDto.getDescription());
//
//            return null;
//        }).when(categoryMapper).updateCategory(Mockito.eq(category), Mockito.eq(requestDto));
//
//        Mockito.when(categoryRepository.save(category)).thenReturn(category);
//
//        CategoryResponseDto actual = categoryService.update(categoryId, requestDto);
//
//        verify(categoryRepository).findById(categoryId);
//        assertEquals(actual.name(), requestDto.getName());
//
//    }


    @Test
    void update_WithInValidId_ShouldThrowEntityNotFound() {
        Long categoryId = -100L;

        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("test");

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            categoryService.update(categoryId,requestDto);
        });

        String actual = exception.getMessage();

        assertEquals("Cannot find Category with id: " + categoryId, actual);

    }
}
