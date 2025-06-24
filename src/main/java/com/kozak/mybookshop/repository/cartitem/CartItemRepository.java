package com.kozak.mybookshop.repository.cartitem;

import com.kozak.mybookshop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
