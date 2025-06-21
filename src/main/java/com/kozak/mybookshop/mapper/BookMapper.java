package com.kozak.mybookshop.mapper;

import com.kozak.mybookshop.config.MapperConfig;
import com.kozak.mybookshop.dto.book.BookDto;
import com.kozak.mybookshop.dto.book.BookDtoWithoutCategoryIds;
import com.kozak.mybookshop.dto.book.CreateBookRequestDto;
import com.kozak.mybookshop.model.Book;
import com.kozak.mybookshop.model.Category;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookDto toBookDto(Book book);

    @Mapping(target = "categories", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    void updateBook(@MappingTarget Book book, CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget Book book, CreateBookRequestDto requestDto) {
        if (requestDto.getCategoryIds() != null) {
            Set<Category> categorySet = requestDto.getCategoryIds().stream()
                    .map(id -> {
                                Category category = new Category();
                                category.setId(id);
                                return category;
                            }
                    )
                    .collect(Collectors.toSet());
            book.setCategories(categorySet);
        }
    }

    @AfterMapping
    default void setCategories(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            List<Long> categoryIdsList = book.getCategories().stream()
                    .map(Category::getId)
                    .toList();
            bookDto.setCategoryIds(categoryIdsList);
        }
    }
}
