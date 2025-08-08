package com.kozak.mybookshop.controller;

import static com.kozak.mybookshop.util.CartItemDataTest.createSampleCartItemDto;
import static com.kozak.mybookshop.util.CartItemDataTest.sampleCartItemDto;
import static com.kozak.mybookshop.util.ShoppingCartDataTest.sampleShoppingCartDto;
import static com.kozak.mybookshop.util.UserDataTest.sampleUser;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kozak.mybookshop.dto.cartitem.CartItemDto;
import com.kozak.mybookshop.dto.cartitem.CartItemQuantityRequestDto;
import com.kozak.mybookshop.dto.cartitem.CreateCartItemRequestDto;
import com.kozak.mybookshop.dto.shoppingcart.ShoppingCartDto;
import com.kozak.mybookshop.model.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext webApplicationContext,
                          @Autowired DataSource dataSource) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        tearDown(dataSource);
    }

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/add-books-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/cartitem/add-cartitem-table.sql")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        User testUser = sampleUser();

        Authentication auth = new UsernamePasswordAuthenticationToken(
                testUser,
                null,
                testUser.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void teardown(@Autowired DataSource dataSource) {
        tearDown(dataSource);
    }

    @SneakyThrows
    static void tearDown(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/cartitem/remove-cartitem-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/remove-books-table.sql")
            );
        }
    }

    @DisplayName("Update quantity book when valid id provided")
    @Test
    void updateQuantityById_WithValidId_ShouldReturnShoppingCartDto() throws Exception {
        CartItemDto cartItem = sampleCartItemDto();
        cartItem.setQuantity(2);
        Set<CartItemDto> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        ShoppingCartDto expected = sampleShoppingCartDto();
        expected.setCartItems(cartItems);
        CartItemQuantityRequestDto requestDto = new CartItemQuantityRequestDto();
        int quantity = 2;
        requestDto.setQuantity(quantity);
        Long id = 1L;

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(put("/cart/items/{id}", id)
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartDto.class);
        assertEquals(expected, actual);
    }

    @DisplayName("Get shopping cart in current user")
    @Test
    void getShoppingCart_WithGivenShoppingCart_ShouldReturnShoppingCartDto() throws Exception {
        CartItemDto cartItem = sampleCartItemDto();
        Set<CartItemDto> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        ShoppingCartDto expected = sampleShoppingCartDto();
        expected.setCartItems(cartItems);

        MvcResult result = mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartDto.class);

        assertEquals(expected, actual);
    }

    @DisplayName("Add cart item when valid request provided")
    @Test
    void addCartItem_WithValidRequest_ShouldReturnShoppingCartDto() throws Exception {
        CartItemDto cartItem = sampleCartItemDto();
        CartItemDto expectedCartItem = createSampleCartItemDto();
        Set<CartItemDto> cartItems = new HashSet<>();
        cartItems.add(expectedCartItem);
        cartItems.add(cartItem);
        ShoppingCartDto expected = sampleShoppingCartDto();
        expected.setCartItems(cartItems);
        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto();
        requestDto.setQuantity(2);
        requestDto.setBookId(3L);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartDto.class);
        assertEquals(expected, actual);
    }

    @DisplayName("Delete cart item when valid id provided")
    @Test
    void deleteCartItem_WithValidId_ShouldReturnNoContentStatus() throws Exception {
        Long cartId = 1L;

        mockMvc.perform(delete("/cart/{id}", cartId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
