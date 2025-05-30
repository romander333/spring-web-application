package com.kozak.mybookshop.repository.book;

import com.kozak.mybookshop.dto.BookSearchParametersDto;
import com.kozak.mybookshop.model.Book;
import com.kozak.mybookshop.repository.SpecificationBuilder;
import com.kozak.mybookshop.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String AUTHOR = "author";
    private static final String TITLE = "title";
    private static final int MINIMUM_LENGTH = 0;
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        Specification<Book> specification = Specification.where(null);
        if (searchParametersDto.titles() != null
                && searchParametersDto.titles().length > MINIMUM_LENGTH) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider(TITLE)
                    .getSpecification(searchParametersDto.titles()));
        }
        if (searchParametersDto.authors() != null
                && searchParametersDto.authors().length > MINIMUM_LENGTH) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider(AUTHOR)
                    .getSpecification(searchParametersDto.authors()));
        }
        return specification;
    }
}
