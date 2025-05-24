package com.kozak.mybookshop.service;

import com.kozak.mybookshop.dto.BookDto;
import com.kozak.mybookshop.dto.CreateBookRequestDto;
import com.kozak.mybookshop.exception.EntityNotFoundException;
import com.kozak.mybookshop.mapper.BookMapper;
import com.kozak.mybookshop.model.Book;
import com.kozak.mybookshop.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toBookDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toBookDto)
                .toList();
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
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found by id:" + id);
        }

        Book book = bookMapper.toModel(requestDto);
        book.setId(id);
        bookRepository.updateBookById(id,book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPrice(),
                book.getDescription(),
                book.getCoverImage());

        return bookMapper.toBookDto(book);

    }
}
