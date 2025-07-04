package com.kozak.mybookshop.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kozak.mybookshop.dto.book.BookDtoWithoutCategoryIds;
import com.kozak.mybookshop.dto.category.CategoryRequestDto;
import com.kozak.mybookshop.dto.category.CategoryResponseDto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static final String COVER_IMAGE =
            "https://i.pinimg.com/originals/9f/a8/9e/9fa89e944234f7c3fe04bacc6da638b2.png";

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) {
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/book/add-books-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/category/add-categories-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/books_categories/add-books-categories-table.sql")
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
                    new ClassPathResource("database/category/remove-categories-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/remove-books-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/books_categories/remove-books-categories-table.sql")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Create a new category when valid request provide")
    @Test
    void createCategory_WithValidRequest_ShouldCreateAndReturnCategoryDto() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("noveliaes");
        requestDto.setDescription("nice novel");
        CategoryResponseDto expected = new CategoryResponseDto(3L, "noveliaes", "nice novel");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryResponseDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        CategoryResponseDto.class);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(actual,expected));

    }

    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Get all categories")
    @Test
    void getAll_GivenBooksInCatalog_ShouldReturnAllCategories() throws Exception {
        List<CategoryResponseDto> expected = List.of(
                new CategoryResponseDto(1L, "roman", "about love people"),
                new CategoryResponseDto(2L, "fantastic", "about not real situation")
        );

        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode node = jsonNode.get("content");
        CategoryResponseDto[] actual = objectMapper.treeToValue(node,CategoryResponseDto[].class);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected.get(0), actual[0]));
        Assertions.assertEquals(expected.size(), actual.length);
    }

    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Should return CategoryDto when valid category id is provided")
    @Test
    void getCategoryById_WithValidId_ShouldReturnCategoryDto() throws Exception {
        Long categoryId = 2L;
        CategoryResponseDto expected =
                new CategoryResponseDto(2L, "fantastic", "about not real situation");

        MvcResult result = mockMvc.perform(get("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        CategoryResponseDto.class);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Update category when valid category id provided")
    @Test
    void updateCategory_WithValidRequestAndId_ReturnCategoryDto() throws Exception {
        Long categoryId = 2L;
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("noveliaes");
        requestDto.setDescription("nice novel");
        CategoryResponseDto expected = new CategoryResponseDto(2L, "noveliaes", "nice novel");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(put("/categories/{id}", categoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual =
                objectMapper.readValue(
                        result.getResponse().getContentAsString(), CategoryResponseDto.class);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Should return NoContent status when valid category id is provided")
    @Test
    void deleteCategory_WithValidId_ShouldReturnNoContentStatus() throws Exception {
        Long categoryId = 2L;
        mockMvc.perform(delete("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Get books when valid category id provided")
    @Test
    void getBooksByCategory_WithValidId_ShouldReturnAllBooks() throws Exception {
        Long categoryId = 2L;
        List<BookDtoWithoutCategoryIds> expected = List.of(
                new BookDtoWithoutCategoryIds()
                        .setId(2L)
                        .setTitle("Super_man")
                        .setAuthor("Andrew")
                        .setIsbn("3323")
                        .setPrice(BigDecimal.valueOf(150))
                        .setDescription("nice book for old people")
                        .setCoverImage(COVER_IMAGE),
                new BookDtoWithoutCategoryIds()
                        .setId(3L)
                        .setTitle("Older_man_in_sea")
                        .setAuthor("Katerina")
                        .setIsbn("3313")
                        .setPrice(BigDecimal.valueOf(300))
                        .setDescription("nice book for old people")
                        .setCoverImage(COVER_IMAGE)
        );

        MvcResult result = mockMvc.perform(get("/categories/{id}/books", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDtoWithoutCategoryIds> actual =
                objectMapper.readValue(
                        result.getResponse().getContentAsString(), new TypeReference<>() {});
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected.get(0).getTitle(), actual.get(0).getTitle());
        Assertions.assertEquals(expected.get(0).getIsbn(), actual.get(0).getIsbn());
    }
}
