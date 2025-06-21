package com.kozak.mybookshop.service.shoppingcart;

import com.kozak.mybookshop.dto.cartitem.CreateCartItemRequestDto;
import com.kozak.mybookshop.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto addCartItem(CreateCartItemRequestDto requestDto);

    ShoppingCartDto getShoppingCart();
}
