package com.kozak.mybookshop.repository;

import com.kozak.mybookshop.model.Book;
import com.kozak.mybookshop.model.Category;
import com.kozak.mybookshop.repository.book.BookRepository;
import org.hibernate.annotations.SQLDelete;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashSet;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("find books by category id")
    @Sql()
    void findByCategoriesId_WithValidCategoriesId_ReturnCorrectBooks() {
        Set<Category> categories = new HashSet<>();
        Category category = new Category();
        category.setDescription("test");
        category.setName("test");
        categories.add(category);
        Book book = new Book();
        book.setAuthor("Kozak");
        book.setTitle("The Book");
        book.setIsbn("978-3-16-148410-0");
        book.setCategories(categories);

    }
}
