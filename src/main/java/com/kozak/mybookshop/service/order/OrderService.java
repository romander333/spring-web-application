package com.kozak.mybookshop.service.order;

import com.kozak.mybookshop.dto.order.OrderDto;
import com.kozak.mybookshop.dto.order.OrderRequestDto;
import com.kozak.mybookshop.dto.order.OrderStatusRequestDto;
import com.kozak.mybookshop.dto.orderitem.OrderItemDto;
import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderRequestDto requestDto);

    List<OrderDto> getAllOrders();

    OrderDto updateStatus(Long id, OrderStatusRequestDto requestDto);

    List<OrderItemDto> getOrderItems(Long id);

    OrderItemDto getOrderItemById(Long orderId, Long itemId);
}
