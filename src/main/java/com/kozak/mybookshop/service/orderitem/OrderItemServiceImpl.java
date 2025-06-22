package com.kozak.mybookshop.service.orderitem;

import com.kozak.mybookshop.dto.orderitem.OrderItemDto;
import com.kozak.mybookshop.repository.orderitem.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderItemDto getOrderItemById(Long id) {
        return null;
    }

    @Override
    public void deleteOrderItemById(Long id) {
        orderItemRepository.deleteById(id);
    }
}
