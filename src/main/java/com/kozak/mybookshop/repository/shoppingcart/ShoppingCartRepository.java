package com.kozak.mybookshop.repository.shoppingcart;

import com.kozak.mybookshop.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findShoppingCartByUser_Id(Long userId);
}
