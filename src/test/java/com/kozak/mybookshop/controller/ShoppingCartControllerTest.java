package com.kozak.mybookshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kozak.mybookshop.dto.cartitem.CartItemDto;
import com.kozak.mybookshop.dto.cartitem.CartItemQuantityRequestDto;
import com.kozak.mybookshop.dto.cartitem.CreateCartItemRequestDto;
import com.kozak.mybookshop.dto.shoppingcart.ShoppingCartDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
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
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static com.kozak.mybookshop.util.CartItemDataTest.sampleCartItemDto;
import static com.kozak.mybookshop.util.ShoppingCartDataTest.sampleShoppingCartDto;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {

    @Autowired
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
    @WithMockUser(username = "romander@gmail.com", roles = "USER")
    @Test
    void updateQuantityById_WithValidId_ShouldReturnShoppingCartDto() throws Exception {
        Long id = 1L;
        int quantity = 2;
        CartItemDto cartItem = sampleCartItemDto();
        cartItem.setQuantity(5);
        Set<CartItemDto> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        ShoppingCartDto expected = sampleShoppingCartDto();
        expected.setCartItems(cartItems);
        CartItemQuantityRequestDto requestDto = new CartItemQuantityRequestDto();
        requestDto.setQuantity(quantity);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult mvcResult = mockMvc.perform(put("/cart/items/{id}", id)
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ShoppingCartDto.class);
        CartItemDto actualCartItem = actual.getCartItems().stream()
                .filter(cart -> cart.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new AssertionError("CartItem with id " + id + " not found"));
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getCartItems().size(), actual.getCartItems().size());
        assertEquals(quantity, actualCartItem.getQuantity());
    }

    @DisplayName("Get shopping cart in current user")
    @WithMockUser(username = "romander@gmail.com", roles = "USER")
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

        ShoppingCartDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), ShoppingCartDto.class);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getCartItems().size(), actual.getCartItems().size());

        for (CartItemDto expectedItem : expected.getCartItems()) {
            CartItemDto actualItem = actual.getCartItems().stream()
                    .filter(item -> item.getId().equals(expectedItem.getId()))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("CartItem with id " + expectedItem.getId() + " not found"));

            assertEquals(expectedItem.getBookId(), actualItem.getBookId());
            assertEquals(expectedItem.getQuantity(), actualItem.getQuantity());
        }
    }

    @DisplayName("Add cart item when valid request provided")
    @WithMockUser(username = "romander@gmail.com", roles = "USER")
    @Test
    void addCartItem_WithValidRequest_ShouldReturnShoppingCartDto() throws Exception {
        Long cartId = 4L;
        CartItemDto cartItem = sampleCartItemDto();
        CartItemDto expectedCartItem = new CartItemDto()
                .setId(4L)
                .setQuantity(2)
                .setBookId(3L);
        Set<CartItemDto> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        cartItems.add(expectedCartItem);
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

        ShoppingCartDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), ShoppingCartDto.class);
        CartItemDto actualCartItem = actual.getCartItems().stream()
                .filter(cart -> cart.getId().equals(cartId))
                .findFirst()
                .orElseThrow(() -> new AssertionError("CartItem with id " + cartId + " not found"));
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getCartItems().size(), actual.getCartItems().size());

        assertEquals(expectedCartItem.getId(), actualCartItem.getId());
        assertEquals(expectedCartItem.getQuantity(), actualCartItem.getQuantity());
        assertEquals(expectedCartItem.getBookId(), actualCartItem.getBookId());
    }

    @DisplayName("Delete cart item when valid id provided")
    @WithMockUser(username = "romander@gmail.com", roles = "USER")
    @Test
    void deleteCartItem_WithValidId_ShouldReturnNoContentStatus() throws Exception {
        Long cartId = 1L;

        mockMvc.perform(delete("/cart/{id}", cartId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
