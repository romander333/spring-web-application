package com.kozak.mybookshop.repository;

import static org.junit.Assert.assertEquals;

import com.kozak.mybookshop.model.Book;
import com.kozak.mybookshop.repository.book.BookRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/category/add-categories-table.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/book/add-books-table.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/books_categories/add-books-categories-table.sql"));
        }
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/books_categories/remove-books-categories-table.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/book/remove-books-table.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/category/remove-categories-table.sql"));
        }
    }

    @Test
    @DisplayName("find books when valid category id provided")
    void findByCategoriesId_WithValidCategoriesId_ReturnBooks() {
        List<Book> actual = bookRepository.findByCategories_Id(1L);
        assertEquals(1, actual.size());
    }
}
