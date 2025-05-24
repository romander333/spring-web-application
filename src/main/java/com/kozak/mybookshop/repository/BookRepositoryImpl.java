package com.kozak.mybookshop.repository;

import com.kozak.mybookshop.exception.DataProcessingException;
import com.kozak.mybookshop.model.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    private final EntityManagerFactory emf;

    @Override
    public Book save(Book book) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = emf.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new DataProcessingException("Cannot save book:" + book, e);
        }
    }

    @Override
    public List<Book> findAll() {
        try (EntityManager entityManager = emf.createEntityManager()) {
            return entityManager.createQuery("from Book",Book.class).getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Cannot retrieve books from DB", e);
        }
    }

    @Override
    public Optional<Book> findById(int id) {
        try (EntityManager entityManager = emf.createEntityManager()) {
            Book book = entityManager.find(Book.class, id);
            return Optional.ofNullable(book);
        }
    }
}
