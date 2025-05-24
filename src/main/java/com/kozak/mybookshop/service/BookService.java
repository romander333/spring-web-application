package com.kozak.mybookshop.service;

import com.kozak.mybookshop.model.Book;

import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
