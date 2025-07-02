package com.kozak.mybookshop.service.order;

import com.kozak.mybookshop.dto.order.OrderDto;
import com.kozak.mybookshop.dto.order.OrderRequestDto;
import com.kozak.mybookshop.dto.order.OrderStatusRequestDto;
import com.kozak.mybookshop.dto.orderitem.OrderItemDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto createOrder(OrderRequestDto requestDto);

    Page<OrderDto> getAllOrders(Pageable pageable);

    OrderDto updateStatus(Long id, OrderStatusRequestDto requestDto);

    List<OrderItemDto> getOrderItems(Long id);

    OrderItemDto getOrderItemById(Long orderId, Long itemId);
}
