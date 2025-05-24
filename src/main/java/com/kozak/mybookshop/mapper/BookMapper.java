package com.kozak.mybookshop.mapper;

import com.kozak.mybookshop.dto.BookDto;
import com.kozak.mybookshop.dto.CreateBookRequestDto;
import com.kozak.mybookshop.mapperconfig.MapperConfig;
import com.kozak.mybookshop.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toBookDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
