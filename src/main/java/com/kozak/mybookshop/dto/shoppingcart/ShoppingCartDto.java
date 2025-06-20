package com.kozak.mybookshop.dto.shoppingcart;

import com.kozak.mybookshop.dto.cartItem.CartItemDto;
import java.util.Set;

public record ShoppingCartDto(Long id, Long userId, Set<CartItemDto> cartItems) {
}
