package com.kozak.mybookshop.service;

import com.kozak.mybookshop.dto.cartitem.CartItemDto;
import com.kozak.mybookshop.dto.cartitem.CartItemQuantityRequestDto;
import com.kozak.mybookshop.dto.cartitem.CreateCartItemRequestDto;
import com.kozak.mybookshop.dto.shoppingcart.ShoppingCartDto;
import com.kozak.mybookshop.mapper.ShoppingCartMapper;
import com.kozak.mybookshop.model.Book;
import com.kozak.mybookshop.model.CartItem;
import com.kozak.mybookshop.model.ShoppingCart;
import com.kozak.mybookshop.model.User;
import com.kozak.mybookshop.repository.book.BookRepository;
import com.kozak.mybookshop.repository.shoppingcart.ShoppingCartRepository;
import com.kozak.mybookshop.security.AuthenticationService;
import com.kozak.mybookshop.service.shoppingcart.ShoppingCartServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;
    
    @Test
    @DisplayName("Update quantity books when valid cart items provided")
    void updateQuantityById_WithValidCartItemId_ShouldReturnShoppingCartDto() {
        Long shoppingCartId = 1L;
        CartItemQuantityRequestDto requestDto = new CartItemQuantityRequestDto();
        requestDto.setQuantity(5);

        CartItemDto cartItemDto = new CartItemDto()
                .setQuantity(5)
                .setBookId(1L)
                .setBookTitle("New_man")
                .setId(1L);
        Set<CartItemDto> cartItemDtos = new HashSet<>();
        cartItemDtos.add(cartItemDto);

        User user = new User();
        user.setId(1L);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(shoppingCartId);

        CartItem cartItem = new CartItem();
        cartItem.setId(shoppingCartId);
        cartItem.setQuantity(2);
        cartItem.setShoppingCart(shoppingCart);
        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);

        shoppingCart.setCartItems(cartItems);

        ShoppingCartDto dto = new ShoppingCartDto()
                .setId(1L)
                .setUserId(1L)
                .setCartItems(cartItemDtos);

        ShoppingCart savedShoppingCart = new ShoppingCart();
        savedShoppingCart.setId(shoppingCartId);

        Mockito.when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(dto);
        Mockito.when(shoppingCartRepository.findShoppingCartByUser_Id(1L)).thenReturn(Optional.of(shoppingCart));
        Mockito.when(shoppingCartRepository.save(shoppingCart)).thenReturn(savedShoppingCart);
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);

        ShoppingCartDto actual = shoppingCartService.updateQuantityById(shoppingCartId, requestDto);

        Assertions.assertTrue(EqualsBuilder.reflectionEquals(dto, actual));

    }

    @Test
    void createShoppingCart_WithValidShoppingRequest_ShouldReturnShoppingCart() {
        User user = new User();
        user.setId(3L);

        ShoppingCart savedShoppingCart = new ShoppingCart();
        savedShoppingCart.setId(3L);
        savedShoppingCart.setUser(user);

        ShoppingCartDto dto = new ShoppingCartDto()
                .setId(3L)
                .setUserId(3L);

        Mockito.when(shoppingCartMapper.toDto(Mockito.any(ShoppingCart.class))).thenReturn(dto);
        Mockito.when(shoppingCartRepository.save(Mockito.any(ShoppingCart.class))).thenReturn(savedShoppingCart);

        ShoppingCartDto actual = shoppingCartService.createShoppingCart(user);

        Assertions.assertTrue(EqualsBuilder.reflectionEquals(dto, actual));

    }


    @Test
    void deleteCartItem_WithValidId_ShouldDeleteShoppingCart() {
        Long id = 3L;
        User user = new User();
        user.setId(id);
        CartItem cartItem = new CartItem();
        cartItem.setId(id);
        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(id);
        shoppingCart.setUser(user);
        shoppingCart.setCartItems(cartItems);

        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);
        Mockito.when(shoppingCartRepository.findShoppingCartByUser_Id(id)).thenReturn(Optional.of(shoppingCart));

        shoppingCartService.deleteCartItem(id);
        Assertions.assertTrue(shoppingCart.getCartItems().isEmpty());
    }

    @Test
    void addCartItem_WithValidCartItem_ShouldReturnShoppingCartDto() {
        Long shoppingCartId = 1L;
        User user = new User();
        user.setId(1L);

        Book book = new Book();
        book.setId(1L);

        CartItem cartItem = new CartItem();
        cartItem.setId(shoppingCartId);
        cartItem.setBook(book);

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(shoppingCartId);
        cartItemDto.setQuantity(5);
        cartItemDto.setBookId(1L);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(shoppingCartId);
        shoppingCart.setCartItems(new HashSet<>());

        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto();
        requestDto.setQuantity(5);
        requestDto.setBookId(1L);

        shoppingCart.getCartItems().add(cartItem);
        ShoppingCart savedShoppingCart = new ShoppingCart();
        savedShoppingCart.setId(shoppingCartId);
        savedShoppingCart.setCartItems(Set.of(cartItem));

        ShoppingCartDto dto = new ShoppingCartDto()
                .setId(shoppingCartId)
                .setUserId(1L)
                .setCartItems(Set.of(cartItemDto));

        Mockito.when(shoppingCartMapper.toDto(Mockito.any(ShoppingCart.class))).thenReturn(dto);
        Mockito.when(shoppingCartRepository.save(Mockito.any(ShoppingCart.class))).thenReturn(savedShoppingCart);
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);
        Mockito.when(shoppingCartRepository.findShoppingCartByUser_Id(shoppingCartId)).thenReturn(Optional.of(shoppingCart));
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book()));

        ShoppingCartDto actual = shoppingCartService.addCartItem(requestDto);

        Assertions.assertTrue(EqualsBuilder.reflectionEquals(dto, actual));
    }

    @Test
    void getShoppingCart_WithValidUser_ShouldReturnShoppingCartDto() {
        Long shoppingCartId = 1L;
        User user = new User();
        user.setId(1L);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(shoppingCartId);
        shoppingCart.setUser(user);
        ShoppingCartDto dto = new ShoppingCartDto()
                .setId(shoppingCartId);

        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);
        Mockito.when(shoppingCartMapper.toDto(Mockito.any(ShoppingCart.class))).thenReturn(dto);
        Mockito.when(shoppingCartRepository.findShoppingCartByUser_Id(shoppingCartId)).thenReturn(Optional.of(shoppingCart));

        ShoppingCartDto actual = shoppingCartService.getShoppingCart();

        Assertions.assertTrue(EqualsBuilder.reflectionEquals(dto, actual));
    }
}
