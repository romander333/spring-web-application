package com.kozak.mybookshop.repository;

import com.kozak.mybookshop.model.Book;

import java.util.List;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();
}
