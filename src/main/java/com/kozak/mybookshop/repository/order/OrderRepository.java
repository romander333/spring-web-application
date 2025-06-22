package com.kozak.mybookshop.repository.order;

import com.kozak.mybookshop.model.Order;
import com.kozak.mybookshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
