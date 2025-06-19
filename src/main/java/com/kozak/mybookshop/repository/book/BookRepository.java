package com.kozak.mybookshop.repository.book;

import com.kozak.mybookshop.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query(value = "select b from Book b JOIN b.categories c where c.id = ?1")
    List<Book> findAllByCategoryId(Long id);
}
