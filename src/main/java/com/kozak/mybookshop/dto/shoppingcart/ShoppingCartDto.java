package com.kozak.mybookshop.dto.shoppingcart;

import com.kozak.mybookshop.dto.cartitem.CartItemDto;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItemSet;
}
