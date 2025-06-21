package com.kozak.mybookshop.repository.book;

import com.kozak.mybookshop.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    List<Book> findByCategories_Id(Long id);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.categories WHERE b.id = :id")
    Optional<Book> findById(Long id);
}
