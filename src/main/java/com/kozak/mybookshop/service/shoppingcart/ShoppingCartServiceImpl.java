package com.kozak.mybookshop.service.shoppingcart;

import com.kozak.mybookshop.dto.cartitem.CreateCartItemRequestDto;
import com.kozak.mybookshop.dto.shoppingcart.ShoppingCartDto;
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

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final AuthenticationService authenticationService;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartDto addCartItem(CreateCartItemRequestDto requestDto) {
        User user = authenticationService.getCurrentUser();

        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUser(user);
        if (shoppingCart == null) {
            throw new RuntimeException("Shopping cart not found for user " + user.getId());
        }

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
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUser(currentUser);
        return shoppingCartMapper.toDto(shoppingCart);
    }

}
