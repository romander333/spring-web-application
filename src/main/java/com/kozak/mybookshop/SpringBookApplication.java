package com.kozak.mybookshop;

import com.kozak.mybookshop.model.Book;
import com.kozak.mybookshop.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class SpringBookApplication {

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBookApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book irrationalThink = new Book();
            irrationalThink.setTitle("Irrational Think");
            irrationalThink.setAuthor("Den Ariel");
            irrationalThink.setIsbn("978-3-16-148410-0");
            irrationalThink.setPrice(BigDecimal.valueOf(100));

            bookService.save(irrationalThink);

            System.out.println(bookService.findAll());
        };
    }
}
