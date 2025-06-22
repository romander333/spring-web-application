package com.kozak.mybookshop.service.order;

import com.kozak.mybookshop.dto.order.OrderDto;
import com.kozak.mybookshop.dto.order.OrderRequestDto;
import com.kozak.mybookshop.dto.orderitem.OrderItemDto;
import com.kozak.mybookshop.mapper.OrderMapper;
import com.kozak.mybookshop.model.*;
import com.kozak.mybookshop.repository.cartitem.CartItemRepository;
import com.kozak.mybookshop.repository.order.OrderRepository;
import com.kozak.mybookshop.repository.orderitem.OrderItemRepository;
import com.kozak.mybookshop.repository.shoppingcart.ShoppingCartRepository;
import com.kozak.mybookshop.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AuthenticationService authenticationService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public OrderDto createOrder(OrderRequestDto requestDto) {
        User user = authenticationService.getCurrentUser();

        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUser(user);
        Set<CartItem> cartItems = new HashSet<>(shoppingCart.getCartItems());
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("No cart items found by the userId" + user.getId());
        }
        BigDecimal total = cartItems.stream()
                .map(item  -> BigDecimal.valueOf(item.getQuantity()).multiply(item .getBook().getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING);
        order.setShippingAddress(requestDto.getShippingAddress());
        order.setTotal(total);
        Set<OrderItem> orderItems = new HashSet<>();

        cartItems.forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        });
            order.setOrderItems(orderItems);
        orderRepository.save(order);
        cartItemRepository.deleteCartItemByShoppingCart_Id(shoppingCart.getId());
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto getAllOrders() {
        return null;
    }
}
