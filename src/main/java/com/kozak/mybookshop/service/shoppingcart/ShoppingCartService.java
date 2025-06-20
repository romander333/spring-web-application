package com.kozak.mybookshop.service.shoppingcart;

import com.kozak.mybookshop.dto.cartItem.CreateCartItemRequestDto;
import com.kozak.mybookshop.dto.shoppingcart.ShoppingCartDto;
import org.springframework.data.domain.Pageable;

public interface ShoppingCartService {
    ShoppingCartDto addCartItem(CreateCartItemRequestDto requestDto);

    ShoppingCartDto getShoppingCart();
}
