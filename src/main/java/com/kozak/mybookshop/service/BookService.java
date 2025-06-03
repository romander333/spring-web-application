package com.kozak.mybookshop.service;

import com.kozak.mybookshop.dto.BookDto;
import com.kozak.mybookshop.dto.BookSearchParametersDto;
import com.kozak.mybookshop.dto.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    Page<BookDto> findAll(Pageable pageable);

    BookDto getById(Long id);

    void deleteById(Long id);

    BookDto update(CreateBookRequestDto requestDto, Long id);

    List<BookDto> search(BookSearchParametersDto requestDto);
}
