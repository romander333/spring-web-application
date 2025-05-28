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

    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        Specification<Book> specification = Specification.where(null);
        if (searchParametersDto.title() != null && searchParametersDto.title().length > 0) {
            specification = specification.and(specificationProviderManager.getSpecificationProvider("title").getSpecification(searchParametersDto.title()));
        }
        if (searchParametersDto.author() != null && searchParametersDto.author().length > 0) {
            specification = specification.and(specificationProviderManager.getSpecificationProvider("author").getSpecification(searchParametersDto.author()));
        }
        return specification;
    }
}
