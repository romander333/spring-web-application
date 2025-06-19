package com.kozak.mybookshop.service.book;

import com.kozak.mybookshop.dto.book.BookDto;
import com.kozak.mybookshop.dto.book.BookSearchParametersDto;
import com.kozak.mybookshop.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    Page<BookDto> findAll(Pageable pageable);

    BookDto getById(Long id);

    void deleteById(Long id);

    BookDto update(CreateBookRequestDto requestDto, Long id);

    List<BookDto> search(BookSearchParametersDto requestDto);

    List<BookDto> getBooksByCategoryId(Long categoryId);
}
