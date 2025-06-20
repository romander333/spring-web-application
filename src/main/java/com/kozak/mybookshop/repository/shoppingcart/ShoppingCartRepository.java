package com.kozak.mybookshop.repository.shoppingcart;

import com.kozak.mybookshop.model.ShoppingCart;
import com.kozak.mybookshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    ShoppingCart findByUser(User user);
}
