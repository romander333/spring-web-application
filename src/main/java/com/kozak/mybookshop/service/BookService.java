package com.kozak.mybookshop.service;

import com.kozak.mybookshop.dto.BookDto;
import com.kozak.mybookshop.dto.BookSearchParametersDto;
import com.kozak.mybookshop.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto getById(Long id);

    void deleteById(Long id);

    BookDto update(CreateBookRequestDto requestDto, Long id);

    List<BookDto> search(BookSearchParametersDto requestDto);
}
