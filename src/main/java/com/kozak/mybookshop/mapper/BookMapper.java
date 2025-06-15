package com.kozak.mybookshop.mapper;

import com.kozak.mybookshop.config.MapperConfig;
import com.kozak.mybookshop.dto.BookDto;
import com.kozak.mybookshop.dto.CreateBookRequestDto;
import com.kozak.mybookshop.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toBookDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateBook(@MappingTarget Book book, CreateBookRequestDto requestDto);
}
