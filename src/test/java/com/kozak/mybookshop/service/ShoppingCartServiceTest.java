package com.kozak.mybookshop.service;

import static com.kozak.mybookshop.util.BookDataTest.sampleBook;
import static com.kozak.mybookshop.util.CartItemDataTest.sampleCartItem;
import static com.kozak.mybookshop.util.CartItemDataTest.sampleCartItemDto;
import static com.kozak.mybookshop.util.ShoppingCartDataTest.sampleShoppingCart;
import static com.kozak.mybookshop.util.ShoppingCartDataTest.sampleShoppingCartDto;
import static com.kozak.mybookshop.util.UserDataTest.sampleUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.kozak.mybookshop.dto.cartitem.CartItemDto;
import com.kozak.mybookshop.dto.cartitem.CartItemQuantityRequestDto;
import com.kozak.mybookshop.dto.cartitem.CreateCartItemRequestDto;
import com.kozak.mybookshop.dto.shoppingcart.ShoppingCartDto;
import com.kozak.mybookshop.exception.EntityNotFoundException;
import com.kozak.mybookshop.mapper.ShoppingCartMapper;
import com.kozak.mybookshop.model.Book;
import com.kozak.mybookshop.model.CartItem;
import com.kozak.mybookshop.model.ShoppingCart;
import com.kozak.mybookshop.model.User;
import com.kozak.mybookshop.repository.book.BookRepository;
import com.kozak.mybookshop.repository.shoppingcart.ShoppingCartRepository;
import com.kozak.mybookshop.security.AuthenticationService;
import com.kozak.mybookshop.service.shoppingcart.ShoppingCartServiceImpl;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
        CartItemQuantityRequestDto requestDto = new CartItemQuantityRequestDto();
        requestDto.setQuantity(5);

        CartItemDto cartItemDto = sampleCartItemDto();
        Set<CartItemDto> cartItemDtos = new HashSet<>();
        cartItemDtos.add(cartItemDto);

        ShoppingCart shoppingCart = sampleShoppingCart();

        CartItem cartItem = sampleCartItem();
        cartItem.setShoppingCart(shoppingCart);
        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);

        shoppingCart.setCartItems(cartItems);

        ShoppingCartDto expected = sampleShoppingCartDto()
                .setCartItems(cartItemDtos);

        ShoppingCart savedShoppingCart = sampleShoppingCart();
        Long id = 1L;
        User user = sampleUser();

        Mockito.when(shoppingCartMapper
                .toDto(Mockito.any(ShoppingCart.class))).thenReturn(expected);
        Mockito.when(shoppingCartRepository
                .findShoppingCartByUser_Id(id)).thenReturn(Optional.of(shoppingCart));
        Mockito.when(shoppingCartRepository
                .save(Mockito.any(ShoppingCart.class))).thenReturn(savedShoppingCart);
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);

        ShoppingCartDto actual = shoppingCartService.updateQuantityById(id, requestDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update quantity books when invalid cart items provided")
    void updateQuantityById_WithInvalidCartItemId_ShouldThrowEntityNotFoundException() {
        User user = sampleUser();
        ShoppingCart shoppingCart = sampleShoppingCart();
        CartItemQuantityRequestDto requestDto = new CartItemQuantityRequestDto();
        requestDto.setQuantity(5);
        Long id = 4L;

        Mockito.when(shoppingCartRepository.findShoppingCartByUser_Id(1L))
                .thenReturn(Optional.of(shoppingCart));
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.updateQuantityById(id, requestDto));

        String actual = exception.getMessage();

        assertEquals("CartItem not found by id: " + id, actual);
    }

    @Test
    @DisplayName("Create shopping Cart when valid request provided")
    void createShoppingCart_WithValidShoppingRequest_ShouldReturnShoppingCart() {
        Long id = 3L;
        User user = sampleUser();

        ShoppingCart savedShoppingCart = sampleShoppingCart();

        ShoppingCartDto expected = new ShoppingCartDto()
                .setId(id)
                .setUserId(id);

        Mockito.when(shoppingCartMapper
                .toDto(Mockito.any(ShoppingCart.class))).thenReturn(expected);
        Mockito.when(shoppingCartRepository
                .save(Mockito.any(ShoppingCart.class))).thenReturn(savedShoppingCart);

        ShoppingCartDto actual = shoppingCartService.createShoppingCart(user);
        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("Delete all cart items in given catalog")
    void deleteAllCartItems_WithGivenCatalog_ShouldReturnEmptyCartItems() {
        Long id = 3L;
        CartItem cartItem = new CartItem();
        cartItem.setId(id);
        cartItem.setQuantity(5);
        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        ShoppingCart shoppingCart = sampleShoppingCart();
        shoppingCart.setCartItems(cartItems);

        shoppingCartService.deleteAllCartItems(shoppingCart);

        assertTrue(shoppingCart.getCartItems().isEmpty());
    }

    @Test
    @DisplayName("Delete cart item when valid id provided")
    void deleteCartItem_WithValidId_ShouldDeleteShoppingCart() {
        Long id = 1L;
        CartItem cartItem = new CartItem();
        cartItem.setId(id);
        cartItem.setQuantity(5);
        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        ShoppingCart shoppingCart = sampleShoppingCart();
        shoppingCart.setCartItems(cartItems);
        User user = sampleUser();

        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);
        Mockito.when(shoppingCartRepository
                .findShoppingCartByUser_Id(id)).thenReturn(Optional.of(shoppingCart));

        shoppingCartService.deleteCartItem(id);
        assertTrue(shoppingCart.getCartItems().isEmpty());
    }

    @Test
    @DisplayName("Add cart item when valid cart item request provided")
    void addCartItem_WithValidCartItem_ShouldReturnShoppingCartDto() {
        Book book = sampleBook();
        CartItem cartItem = sampleCartItem();
        cartItem.setBook(book);

        ShoppingCart shoppingCart = sampleShoppingCart();
        shoppingCart.setCartItems(new HashSet<>());
        Long id = 1L;
        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto();
        requestDto.setQuantity(5);
        requestDto.setBookId(id);
        CartItemDto cartItemDto = sampleCartItemDto();
        shoppingCart.getCartItems().add(cartItem);
        ShoppingCart savedShoppingCart = sampleShoppingCart();
        savedShoppingCart.setCartItems(Set.of(cartItem));
        User user = sampleUser();
        ShoppingCartDto expected = sampleShoppingCartDto()
                .setCartItems(Set.of(cartItemDto));

        Mockito.when(shoppingCartMapper
                .toDto(Mockito.any(ShoppingCart.class))).thenReturn(expected);
        Mockito.when(shoppingCartRepository
                .save(Mockito.any(ShoppingCart.class))).thenReturn(savedShoppingCart);
        Mockito.when(authenticationService
                .getCurrentUser()).thenReturn(user);
        Mockito.when(shoppingCartRepository
                .findShoppingCartByUser_Id(id)).thenReturn(Optional.of(shoppingCart));
        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(new Book()));

        ShoppingCartDto actual = shoppingCartService.addCartItem(requestDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get Shopping Cart when valid user provided")
    void getShoppingCart_WithValidUser_ShouldReturnShoppingCartDto() {
        Long id = 1L;
        User user = sampleUser();
        ShoppingCart shoppingCart = sampleShoppingCart();
        ShoppingCartDto expected = sampleShoppingCartDto();

        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);
        Mockito.when(shoppingCartMapper
                .toDto(Mockito.any(ShoppingCart.class))).thenReturn(expected);
        Mockito.when(shoppingCartRepository
                .findShoppingCartByUser_Id(id)).thenReturn(Optional.of(shoppingCart));

        ShoppingCartDto actual = shoppingCartService.getShoppingCart();

        assertEquals(expected, actual);
    }
}
