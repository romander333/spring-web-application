package com.kozak.mybookshop.repository;

import com.kozak.mybookshop.model.Book;
import com.kozak.mybookshop.repository.book.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("find books by category id")
    @Sql(scripts = "classpath:database/category/add-category-to-book-store-test-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/add-books-to-book_store_test-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books_categories/add-books-categiries-id-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByCategoriesId_WithValidCategoriesId_ReturnTwoBooks() {
        List<Book> actual = bookRepository.findByCategories_Id(1L);

        Assertions.assertEquals(1, actual.size());
    }

    @Test
    @Sql(scripts = "classpath:database/category/add-category-to-book-store-test-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/add-books-to-book_store_test-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books_categories/add-books-categiries-id-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findById_WithValidBookId_ReturnTrue() {
        Book actual = bookRepository.findById(1L).get();
        Assertions.assertEquals("New_man", actual.getTitle());
    }
}
