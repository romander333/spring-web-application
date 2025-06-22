package com.kozak.mybookshop.dto.order;

import com.kozak.mybookshop.model.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private Set<OrderItemDto> orderItemDtos;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private String status;
}
