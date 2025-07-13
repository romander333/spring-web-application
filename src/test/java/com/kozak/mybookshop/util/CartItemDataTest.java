package com.kozak.mybookshop.util;

import com.kozak.mybookshop.dto.cartitem.CartItemDto;
import com.kozak.mybookshop.model.CartItem;

public class CartItemDataTest {

    public static CartItem sampleCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setQuantity(2);
        return cartItem;
    }

    public static CartItemDto sampleCartItemDto() {
        return new CartItemDto()
                .setId(1L)
                .setQuantity(2)
                .setBookId(1L)
                .setBookTitle("New_man");
    }
}
