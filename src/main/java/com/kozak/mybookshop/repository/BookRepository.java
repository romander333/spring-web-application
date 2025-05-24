package com.kozak.mybookshop.repository;

import com.kozak.mybookshop.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(int id);
}
