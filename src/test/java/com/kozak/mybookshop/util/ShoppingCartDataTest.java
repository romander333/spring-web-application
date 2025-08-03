package com.kozak.mybookshop.util;

import com.kozak.mybookshop.dto.shoppingcart.ShoppingCartDto;
import com.kozak.mybookshop.model.ShoppingCart;
import com.kozak.mybookshop.model.User;

import static com.kozak.mybookshop.util.UserDataTest.sampleUser;

public class ShoppingCartDataTest {

    public static ShoppingCart sampleShoppingCart() {
        User user = sampleUser();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(user.getId());
        shoppingCart.setUser(user);
        return shoppingCart;
    }

    public static ShoppingCartDto sampleShoppingCartDto() {
        return new ShoppingCartDto()
                .setId(1L)
                .setUserId(1L);
    }

}
