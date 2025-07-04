package com.kozak.mybookshop.service;

import static org.mockito.Mockito.verify;

import com.kozak.mybookshop.dto.book.BookDto;
import com.kozak.mybookshop.dto.book.BookDtoWithoutCategoryIds;
import com.kozak.mybookshop.dto.book.CreateBookRequestDto;
import com.kozak.mybookshop.exception.EntityNotFoundException;
import com.kozak.mybookshop.mapper.BookMapper;
import com.kozak.mybookshop.model.Book;
import com.kozak.mybookshop.model.Category;
import com.kozak.mybookshop.repository.book.BookRepository;
import com.kozak.mybookshop.service.book.BookServiceImpl;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("get book when valid book id provided")
    void getById_WithValidBookId_ShouldReturnBook() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Good_Book");

        BookDto bookDto = new BookDto();
        bookDto.setTitle("Good_Book");

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toBookDto(book)).thenReturn(bookDto);

        String actual = bookService.getById(bookId).getTitle();
        String expected = "Good_Book";

        verify(bookRepository).findById(bookId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("get book by invalid id and expected exception")
    void getById_WithInValidBookId_ShouldThrowEntityNotFoundException() {
        Long bookId = -100L;
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.getById(bookId)
        );
        String actual = exception.getMessage();

        verify(bookRepository).findById(bookId);
        Assertions.assertEquals("Book not found by id:" + bookId, actual);
    }

    @Test
    @DisplayName("get books when valid category id provided ")
    void getBooksByCategoryId_WithValidCategoryId_ShouldReturnBooks() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        Set<Category> categories = new HashSet<>();
        categories.add(category);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Good_Book");
        book.setCategories(categories);
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Bad_Book");
        book2.setCategories(categories);

        BookDtoWithoutCategoryIds dto1 = new BookDtoWithoutCategoryIds();
        dto1.setTitle("Good_Book");
        BookDtoWithoutCategoryIds dto2 = new BookDtoWithoutCategoryIds();
        dto2.setTitle("Bad_Book");

        Mockito.when(bookMapper.toDtoWithoutCategories(book)).thenReturn(dto1);
        Mockito.when(bookMapper.toDtoWithoutCategories(book2)).thenReturn(dto2);
        Mockito.when(bookRepository.findByCategories_Id(categoryId))
                .thenReturn(List.of(book, book2));
        List<BookDtoWithoutCategoryIds> books = bookService.getBooksByCategoryId(categoryId);

        verify(bookRepository).findByCategories_Id(categoryId);
        Assertions.assertEquals(2, books.size());
        Assertions.assertEquals("Good_Book", books.get(0).getTitle());
        Assertions.assertEquals("Bad_Book", books.get(1).getTitle());
    }

    @Test
    @DisplayName("save book when valid book provided")
    void save_WithValidBook_ShouldSaveAndReturnBookDto() {
        Book book = new Book();
        book.setTitle("Good_Book");

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle("Good_Book");

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Good_Book");

        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("Good_Book");

        Mockito.when(bookMapper.toBookDto(savedBook)).thenReturn(bookDto);
        Mockito.when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(savedBook);

        BookDto actual = bookService.save(createBookRequestDto);
        verify(bookRepository).save(book);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("Good_Book", actual.getTitle());
    }

    @Test
    @DisplayName("delete book when valid book id provided")
    void deleteById_WithValidBookId_ShouldDeleteBook() {
        Long bookId = 1L;
        bookService.deleteById(1L);
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    @DisplayName("find all books")
    void findAll_WithValidPageable_ReturnPageOfBookDtos() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Good_Book");
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Bad_Book");

        BookDtoWithoutCategoryIds dto1 = new BookDtoWithoutCategoryIds();
        dto1.setTitle("Good_Book");
        BookDtoWithoutCategoryIds dto2 = new BookDtoWithoutCategoryIds();
        dto2.setTitle("Bad_Book");

        Page<Book> bookPage = new PageImpl<>(List.of(book, book2));
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(bookMapper.toDtoWithoutCategories(book)).thenReturn(dto1);
        Mockito.when(bookMapper.toDtoWithoutCategories(book2)).thenReturn(dto2);
        Mockito.when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        Page<BookDtoWithoutCategoryIds> actual = bookService.findAll(pageable);
        Assertions.assertEquals(2, actual.getContent().size());
        Assertions.assertEquals("Good_Book", actual.getContent().get(0).getTitle());
        Assertions.assertEquals("Bad_Book", actual.getContent().get(1).getTitle());
    }

    @Test
    @DisplayName("update book when invalid book id provided")
    void update_WithInValidId_ShouldThrowEntityNotFoundException() {
        Long bookId = -100L;

        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("Good_Book");

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bookService.update(createBookRequestDto,bookId);
        });
        String actual = exception.getMessage();

        Assertions.assertEquals("Book not found by id:" + bookId, actual);
    }
}
