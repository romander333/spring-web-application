package com.kozak.mybookshop.service.book;

import com.kozak.mybookshop.dto.book.BookDto;
import com.kozak.mybookshop.dto.book.BookSearchParametersDto;
import com.kozak.mybookshop.dto.book.CreateBookRequestDto;
import com.kozak.mybookshop.exception.EntityNotFoundException;
import com.kozak.mybookshop.mapper.BookMapper;
import com.kozak.mybookshop.model.Book;
import com.kozak.mybookshop.repository.book.BookRepository;
import com.kozak.mybookshop.repository.book.BookSpecificationBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toBookDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toBookDto);
    }

    @Override
    public BookDto getById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Book not found by id:" + id));
        return bookMapper.toBookDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    @Override
    public BookDto update(CreateBookRequestDto requestDto, Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Book not found by id:" + id));
        bookMapper.updateBook(book, requestDto);

        return bookMapper.toBookDto(book);
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto requestDto) {
        Specification<Book> specification = bookSpecificationBuilder.build(requestDto);
        return bookRepository.findAll(specification).stream()
                .map(bookMapper::toBookDto)
                .toList();
    }

    @Override
    public List<BookDto> getBooksByCategoryId(Long categoryId) {
        return bookRepository.findAllByCategoryId(categoryId).stream()
                .map(bookMapper::toBookDto)
                .toList();
    }
}
