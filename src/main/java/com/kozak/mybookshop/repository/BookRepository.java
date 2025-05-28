package com.kozak.mybookshop.repository;

import com.kozak.mybookshop.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
