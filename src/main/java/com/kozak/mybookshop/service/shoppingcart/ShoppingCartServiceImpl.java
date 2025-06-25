package com.kozak.mybookshop.service.shoppingcart;

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
import com.kozak.mybookshop.repository.cartitem.CartItemRepository;
import com.kozak.mybookshop.repository.shoppingcart.ShoppingCartRepository;
import com.kozak.mybookshop.security.AuthenticationService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final AuthenticationService authenticationService;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartDto updateQuantityById(Long id, CartItemQuantityRequestDto requestDto) {
        User currentUser = authenticationService.getCurrentUser();
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUser(currentUser)
                .orElseThrow(() ->
                        new EntityNotFoundException("ShoppingCart not found by user id: "
                                + currentUser.getId()));

        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found by id: "
                        + id));
        cartItem.setQuantity(requestDto.getQuantity());
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public void deleteCartItem(Long id) {
        User currentUser = authenticationService.getCurrentUser();
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUser(currentUser)
                .orElseThrow(() ->
                        new EntityNotFoundException("ShoppingCart not found by user id: "
                                + currentUser.getId()));

        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found by id: " + id));
        shoppingCart.getCartItems().remove(cartItem);
    }

    @Override
    public void deleteAllCartItems() {
        User currentUser = authenticationService.getCurrentUser();
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUser(currentUser)
                .orElseThrow(() ->
                        new EntityNotFoundException("ShoppingCart not found by user id: "
                                + currentUser.getId()));

        shoppingCart.getCartItems().clear();
    }

    @Override
    public ShoppingCartDto addCartItem(CreateCartItemRequestDto requestDto) {
        User currentUser = authenticationService.getCurrentUser();

        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUser(currentUser)
                .orElseThrow(() ->
                        new EntityNotFoundException("ShoppingCart not found by user id: "
                                + currentUser.getId()));

        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() ->
                        new RuntimeException("Book not found by id " + requestDto.getBookId()));

        Optional<CartItem> existingItemOpt = shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(book.getId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(requestDto.getQuantity() + existingItem.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setBook(book);
            cartItem.setQuantity(requestDto.getQuantity());
            cartItem.setShoppingCart(shoppingCart);
            shoppingCart.getCartItems().add(cartItem);
        }
        shoppingCartRepository.save(shoppingCart);

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto getShoppingCart() {
        User currentUser = authenticationService.getCurrentUser();
        Optional<ShoppingCart> shoppingCartByUser =
                shoppingCartRepository.findShoppingCartByUser(currentUser);
        return shoppingCartMapper.toDto(shoppingCartByUser
                .orElseThrow(() ->
                        new EntityNotFoundException("ShoppingCart not found by user id: "
                                + currentUser.getId())));
    }
}
