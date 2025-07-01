package com.kozak.mybookshop.repository.order;

import com.kozak.mybookshop.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUser_Id(Long userId, Pageable pageable);
}
