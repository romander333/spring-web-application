package com.kozak.mybookshop.util;

import com.kozak.mybookshop.dto.book.BookDto;
import com.kozak.mybookshop.dto.book.BookDtoWithoutCategoryIds;
import com.kozak.mybookshop.dto.book.CreateBookRequestDto;
import com.kozak.mybookshop.model.Book;
import java.math.BigDecimal;

public class BookDataTest {
    private static final String COVER_IMAGE =
            "https://i.pinimg.com/originals/9f/a8/9e/9fa89e944234f7c3fe04bacc6da638b2.png";

    public static Book sampleBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("New_man");
        book.setAuthor("Roman");
        book.setIsbn("333");
        book.setPrice(BigDecimal.valueOf(95.99));
        book.setCoverImage(COVER_IMAGE);
        return book;
    }

    public static Book anotherSampleBook() {
        Book book = new Book();
        book.setId(2L);
        book.setTitle("Bad_Book");
        book.setAuthor("Madyara");
        book.setIsbn("123321");
        book.setPrice(BigDecimal.valueOf(250));
        book.setCoverImage(COVER_IMAGE);
        return book;
    }

    public static BookDto sampleBookDto() {
        return new BookDto()
                .setId(1L)
                .setTitle("New_man")
                .setAuthor("Roman")
                .setIsbn("333")
                .setPrice(BigDecimal.valueOf(95.99))
                .setCoverImage(COVER_IMAGE);
    }

    public static CreateBookRequestDto sampleCreateBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("New_man")
                .setAuthor("Roman")
                .setIsbn("333")
                .setPrice(BigDecimal.valueOf(95.99))
                .setCoverImage(COVER_IMAGE);
    }

    public static BookDtoWithoutCategoryIds sampleBookDtoWithoutCategoryIds() {
        return new BookDtoWithoutCategoryIds()
                .setAuthor("Roman")
                .setTitle("New_man")
                .setIsbn("333")
                .setPrice(BigDecimal.valueOf(99.99))
                .setCoverImage(COVER_IMAGE);
    }

    public static BookDtoWithoutCategoryIds anotherSampleBookDtoWithoutCategoryIds() {
        return new BookDtoWithoutCategoryIds()
                .setAuthor("Madyara")
                .setTitle("Bad_Book")
                .setIsbn("123321")
                .setPrice(BigDecimal.valueOf(250))
                .setCoverImage(COVER_IMAGE);
    }
}
