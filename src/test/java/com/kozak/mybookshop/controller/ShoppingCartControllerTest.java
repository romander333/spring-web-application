package com.kozak.mybookshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kozak.mybookshop.dto.cartitem.CartItemDto;
import com.kozak.mybookshop.dto.cartitem.CartItemQuantityRequestDto;
import com.kozak.mybookshop.dto.shoppingcart.ShoppingCartDto;
import com.kozak.mybookshop.dto.user.UserLoginRequestDto;
import com.kozak.mybookshop.dto.user.UserLoginResponseDto;
import com.kozak.mybookshop.model.CartItem;
import com.kozak.mybookshop.model.ShoppingCart;
import com.kozak.mybookshop.model.User;
import com.kozak.mybookshop.security.AuthenticationService;
import com.kozak.mybookshop.security.JwtAuthenticationFilter;
import com.kozak.mybookshop.service.user.CustomUserDetailsService;
import com.kozak.mybookshop.service.user.UserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import static com.kozak.mybookshop.util.CartItemDataTest.sampleCartItem;
import static com.kozak.mybookshop.util.CartItemDataTest.sampleCartItemDto;
import static com.kozak.mybookshop.util.ShoppingCartDataTest.sampleShoppingCart;
import static com.kozak.mybookshop.util.ShoppingCartDataTest.sampleShoppingCartDto;
import static com.kozak.mybookshop.util.UserDataTest.sampleAuthentication;
import static com.kozak.mybookshop.util.UserDataTest.sampleUser;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {

    @Autowired
    protected static MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationFilter authFilter;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private CustomUserDetailsService userService;

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
        }
    }

    @WithMockUser(username = "romander@gmail.com", roles = "USER")
    @Test
    void updateQuantityById_WithValidId_ShouldReturnShoppingCartDto() throws Exception {

        Long id = 1L;
        CartItemDto cartItem = sampleCartItemDto();
        Set<CartItemDto> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        ShoppingCartDto expected = sampleShoppingCartDto();
        expected.setCartItems(cartItems);
        CartItemQuantityRequestDto requestDto = new CartItemQuantityRequestDto();
        requestDto.setQuantity(2);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(put("/cart/items/{id}", id)
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());

        ShoppingCartDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ShoppingCartDto.class);

        assertEquals(expected.getId(), actual.getId());

    }


}
