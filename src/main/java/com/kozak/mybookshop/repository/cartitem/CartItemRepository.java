package com.kozak.mybookshop.repository.cartitem;

import com.kozak.mybookshop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("update CartItem c set c.quantity = :quantity where c.id = :id")
    @Modifying
    void updateQuantityById(int quantity, Long id);
}
