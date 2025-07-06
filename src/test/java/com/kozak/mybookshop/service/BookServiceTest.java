package com.kozak.mybookshop.service;

import static com.kozak.mybookshop.util.BookDataTest.anotherSampleBook;
import static com.kozak.mybookshop.util.BookDataTest.anotherSampleBookDtoWithoutCategoryIds;
import static com.kozak.mybookshop.util.BookDataTest.sampleBook;
import static com.kozak.mybookshop.util.BookDataTest.sampleBookDto;
import static com.kozak.mybookshop.util.BookDataTest.sampleBookDtoWithoutCategoryIds;
import static com.kozak.mybookshop.util.BookDataTest.sampleCreateBookRequestDto;
import static com.kozak.mybookshop.util.CategoryDataTest.sampleCategory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import com.kozak.mybookshop.util.BookDataTest;
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

    @Mock
    private BookDataTest testUtil;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("get book when valid book id provided")
    void getById_WithValidBookId_ShouldReturnBook() {
        Long bookId = 1L;
        Book book = sampleBook();

        BookDto expected = sampleBookDto();

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toBookDto(book)).thenReturn(expected);

        BookDto actual = bookService.getById(bookId);

        verify(bookRepository).findById(bookId);
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.getIsbn(), actual.getIsbn());
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
        assertEquals("Book not found by id:" + bookId, actual);
    }

    @Test
    @DisplayName("get books when valid category id provided ")
    void getBooksByCategoryId_WithValidCategoryId_ShouldReturnBooks() {
        Category category = sampleCategory();
        Set<Category> categories = new HashSet<>();
        categories.add(category);

        Book book = sampleBook();
        book.setCategories(categories);
        Book book2 = anotherSampleBook();
        book2.setCategories(categories);
        Long categoryId = 1L;

        BookDtoWithoutCategoryIds dto1 = sampleBookDtoWithoutCategoryIds();
        BookDtoWithoutCategoryIds dto2 = anotherSampleBookDtoWithoutCategoryIds();

        Mockito.when(bookMapper.toDtoWithoutCategories(book)).thenReturn(dto1);
        Mockito.when(bookMapper.toDtoWithoutCategories(book2)).thenReturn(dto2);
        Mockito.when(bookRepository.findByCategories_Id(categoryId))
                .thenReturn(List.of(book, book2));
        List<BookDtoWithoutCategoryIds> books = bookService.getBooksByCategoryId(categoryId);

        verify(bookRepository).findByCategories_Id(categoryId);
        assertEquals(2, books.size());
        assertEquals("Good_Book", books.get(0).getTitle());
        assertEquals("Bad_Book", books.get(1).getTitle());
    }

    @Test
    @DisplayName("save book when valid book provided")
    void save_WithValidBook_ShouldSaveAndReturnBookDto() {
        Book book = sampleBook();
        Book savedBook = sampleBook();
        BookDto bookDto = sampleBookDto();
        CreateBookRequestDto createBookRequestDto = sampleCreateBookRequestDto();

        Mockito.when(bookMapper.toBookDto(savedBook)).thenReturn(bookDto);
        Mockito.when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(savedBook);

        BookDto actual = bookService.save(createBookRequestDto);
        verify(bookRepository).save(book);
        assertNotNull(actual);
        assertEquals(createBookRequestDto.getTitle(), actual.getTitle());
        assertEquals(createBookRequestDto.getAuthor(), actual.getAuthor());
        assertEquals(createBookRequestDto.getIsbn(), actual.getIsbn());

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
        Book book = sampleBook();
        Book book2 = anotherSampleBook();

        BookDtoWithoutCategoryIds dto1 = sampleBookDtoWithoutCategoryIds();
        BookDtoWithoutCategoryIds dto2 = anotherSampleBookDtoWithoutCategoryIds();

        Page<Book> bookPage = new PageImpl<>(List.of(book, book2));
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(bookMapper.toDtoWithoutCategories(book)).thenReturn(dto1);
        Mockito.when(bookMapper.toDtoWithoutCategories(book2)).thenReturn(dto2);
        Mockito.when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        Page<BookDtoWithoutCategoryIds> actual = bookService.findAll(pageable);
        assertEquals(2, actual.getContent().size());
        assertEquals("Good_Book", actual.getContent().get(0).getTitle());
        assertEquals("Bad_Book", actual.getContent().get(1).getTitle());
    }

    @Test
    @DisplayName("update book when invalid book id provided")
    void update_WithInValidId_ShouldThrowEntityNotFoundException() {
        Long bookId = -100L;

        CreateBookRequestDto createBookRequestDto = sampleCreateBookRequestDto();

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bookService.update(createBookRequestDto,bookId);
        });
        String actual = exception.getMessage();

        assertEquals("Book not found by id:" + bookId, actual);
    }
}
