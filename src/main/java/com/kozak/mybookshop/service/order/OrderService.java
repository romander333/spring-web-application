package com.kozak.mybookshop.service.order;

import com.kozak.mybookshop.dto.order.OrderDto;
import com.kozak.mybookshop.dto.order.OrderRequestDto;

public interface OrderService {
    OrderDto createOrder(OrderRequestDto requestDto);

    OrderDto getAllOrders();
}
