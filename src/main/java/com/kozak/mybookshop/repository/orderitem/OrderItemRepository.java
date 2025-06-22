package com.kozak.mybookshop.repository.orderitem;

import com.kozak.mybookshop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    void deleteOrderItemByOrder_Id(Long orderId);
}
