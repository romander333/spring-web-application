package com.kozak.mybookshop.service.orderitem;

import com.kozak.mybookshop.dto.orderitem.OrderItemDto;

public interface OrderItemService {
    OrderItemDto getOrderItemById(Long id);

    void deleteOrderItemById(Long id);
}
