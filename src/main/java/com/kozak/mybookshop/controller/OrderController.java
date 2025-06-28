package com.kozak.mybookshop.controller;

import com.kozak.mybookshop.dto.order.OrderDto;
import com.kozak.mybookshop.dto.order.OrderRequestDto;
import com.kozak.mybookshop.dto.order.OrderStatusRequestDto;
import com.kozak.mybookshop.dto.orderitem.OrderItemDto;
import com.kozak.mybookshop.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("orders")
@Tag(name = "Orders management", description = "Endpoint for managing orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get orders", description = "Get order history")
    @PreAuthorize("hasRole('USER')")
    public List<OrderDto> getOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}/items")
    @Operation(summary = "Get order items", description = "Get items by order id")
    @PreAuthorize("hasRole('USER')")
    public List<OrderItemDto> getOrderItems(@PathVariable Long id) {
        return orderService.getOrderItems(id);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get order item", description = "Get item by order and item id")
    @PreAuthorize("hasRole('USER')")
    public OrderItemDto getOrderItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        return orderService.getOrderItemById(orderId, itemId);
    }

    @PostMapping
    @Operation(summary = "Create order", description = "Create order by elements in your cart")
    @PreAuthorize("hasRole('USER')")
    public OrderDto createOrder(@RequestBody @Valid OrderRequestDto requestDto) {
        return orderService.createOrder(requestDto);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update status", description = "Update order status")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderDto updateStatus(
            @PathVariable Long id,
            @RequestBody @Valid OrderStatusRequestDto requestDto) {
        return orderService.updateStatus(id,requestDto);
    }
}
