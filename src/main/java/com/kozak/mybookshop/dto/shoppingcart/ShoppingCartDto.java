package com.kozak.mybookshop.dto.shoppingcart;

import com.kozak.mybookshop.dto.cartItem.CartItemDto;
import lombok.Data;

import java.util.Set;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItemSet;
}
