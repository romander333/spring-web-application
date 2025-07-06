package com.kozak.mybookshop.controller;

import static com.kozak.mybookshop.util.BookDataTest.sampleBookDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kozak.mybookshop.dto.book.BookDto;
import com.kozak.mybookshop.dto.book.CreateBookRequestDto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    private static final String COVER_IMAGE =
            "https://i.pinimg.com/originals/9f/a8/9e/9fa89e944234f7c3fe04bacc6da638b2.png";

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) {
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/add-books-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/category/add-categories-table.sql")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/remove-books-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/category/remove-categories-table.sql")
            );
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new book when valid request provide")
    void createBook_WithValidRequest_Success() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Black_man")
                .setAuthor("Kozak")
                .setIsbn("4555")
                .setPrice(BigDecimal.TEN)
                .setCoverImage(COVER_IMAGE)
                .setCategoryIds(List.of(1L, 2L));
        BookDto expected = new BookDto()
                .setId(4L)
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setCoverImage(requestDto.getCoverImage())
                .setCategoryIds(List.of(1L, 2L));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(post("/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        assertNotNull(actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.getIsbn(), actual.getIsbn());
    }

    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Get all books")
    @Test
    void getAll_GivenBooksInCatalog_ShouldReturnAllBooks() throws Exception {
        List<BookDto> expectedBooks = List.of(
                sampleBookDto(),
                new BookDto().setTitle("Super_man")
                        .setAuthor("Andrew")
                        .setIsbn("3323")
                        .setPrice(BigDecimal.valueOf(150)),
                new BookDto().setTitle("Older_man_in_sea")
                        .setAuthor("Katerina")
                        .setIsbn("3313")
                        .setPrice(BigDecimal.valueOf(300))
                );

        MvcResult result = mockMvc.perform(get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode node = jsonNode.get("content");
        BookDto[] bookDtos = objectMapper.treeToValue(node, BookDto[].class);
        assertEquals(expectedBooks.size(), bookDtos.length);
        assertEquals(expectedBooks.get(0).getTitle(), bookDtos[0].getTitle());
        assertEquals(expectedBooks.get(1).getTitle(), bookDtos[1].getTitle());
        assertEquals(expectedBooks.get(2).getIsbn(), bookDtos[2].getIsbn());
    }

    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Should return BookDto when valid book id is provided")
    @Test
    void getBookById_WithValidId_ShouldReturnBookDto() throws Exception {
        Long bookId = 1L;
        BookDto expected = sampleBookDto()
                .setCategoryIds(List.of());

        MvcResult result = mockMvc.perform(get("/books/{id}", bookId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.getIsbn(), actual.getIsbn());
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Should return NoContent status when valid book id is provided")
    @Test
    void deleteBookById_WithValidId_ShouldReturnCorrectStatus() throws Exception {
        Long bookId = 1L;
        mockMvc.perform(delete("/books/{id}", bookId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Update book when valid book id provided")
    @Test
    void updateBookById_WithValidId_ShouldUpdateBookDto() throws Exception {
        Long bookId = 1L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Bad_man")
                .setAuthor("Eva")
                .setIsbn("3455")
                .setPrice(BigDecimal.valueOf(100))
                .setCategoryIds(List.of(1L))
                .setCoverImage(COVER_IMAGE);
        BookDto expected = new BookDto()
                .setId(bookId)
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription())
                .setCategoryIds(requestDto.getCategoryIds())
                .setCoverImage(requestDto.getCoverImage());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(put("/books/{id}", bookId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
        BookDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.getIsbn(), actual.getIsbn());
        assertEquals(expected.getCoverImage(), actual.getCoverImage());
    }

    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Should return list of BookDto when valid titles and authors provided")
    @Test
    void search_WithValidTitle_ShouldReturnBookDto() throws Exception {
        String[] titles = {"New_man"};
        String[] authors = {"Roman"};
        BookDto expected = sampleBookDto();

        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("titles", titles[0])
                        .param("authors", authors[0])
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {});
        assertEquals(expected.getTitle(), actual.get(0).getTitle());
        assertEquals(expected.getAuthor(), actual.get(0).getAuthor());
        assertEquals(expected.getIsbn(), actual.get(0).getIsbn());
    }
}
