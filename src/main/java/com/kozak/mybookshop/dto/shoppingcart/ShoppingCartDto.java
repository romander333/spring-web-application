package com.kozak.mybookshop.dto.shoppingcart;

import com.kozak.mybookshop.dto.cartitem.CartItemDto;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItems;
}
