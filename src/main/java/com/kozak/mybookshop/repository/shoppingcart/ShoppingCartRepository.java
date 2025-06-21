package com.kozak.mybookshop.repository.shoppingcart;

import com.kozak.mybookshop.model.ShoppingCart;
import com.kozak.mybookshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    @Query("""
    SELECT sc FROM ShoppingCart sc
    LEFT JOIN FETCH sc.cartItems ci
    LEFT JOIN FETCH ci.book
    WHERE sc.user = :user
            """)
    ShoppingCart findShoppingCartByUser(User user);
}
