package com.kozak.mybookshop.service.shoppingcart;

import com.kozak.mybookshop.dto.cartitem.CartItemQuantityRequestDto;
import com.kozak.mybookshop.dto.cartitem.CreateCartItemRequestDto;
import com.kozak.mybookshop.dto.shoppingcart.ShoppingCartDto;
import com.kozak.mybookshop.model.User;

public interface ShoppingCartService {
    ShoppingCartDto addCartItem(CreateCartItemRequestDto requestDto);

    ShoppingCartDto getShoppingCart();

    ShoppingCartDto updateQuantityById(Long id, CartItemQuantityRequestDto requestDto);

    ShoppingCartDto createShoppingCart(User user);

    void deleteCartItem(Long id);

    void deleteAllCartItems();
}
