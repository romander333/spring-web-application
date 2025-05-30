package com.kozak.mybookshop.repository.book;

import com.kozak.mybookshop.model.Book;
import com.kozak.mybookshop.repository.SpecificationProvider;
import com.kozak.mybookshop.repository.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationManager implements SpecificationProviderManager<Book> {

    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(s -> s.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "No specification provider found for key: " + key));
    }
}
