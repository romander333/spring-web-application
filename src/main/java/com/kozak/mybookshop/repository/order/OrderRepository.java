package com.kozak.mybookshop.repository.order;

import com.kozak.mybookshop.model.Order;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"orderItems", "orderItems.book"})
    List<Order> findAllByUser_id(Long id);
}
