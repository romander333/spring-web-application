package com.kozak.mybookshop.repository;

import com.kozak.mybookshop.model.Book;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Modifying
    @Query("UPDATE Book b SET b.title = :title, "
            + "b.author = :author, "
            + "b.isbn = :isbn, "
            + "b.price = :price, "
            + "b.description = :description, "
            + "b.coverImage = :coverImage WHERE b.id = :id")
    void updateBookById(@Param("id") Long id,
                        @Param("title") String title,
                        @Param("author") String author,
                        @Param("isbn") String isbn,
                        @Param("price") BigDecimal price,
                        @Param("description") String description,
                        @Param("coverImage") String coverImage);

}
