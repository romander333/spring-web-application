package com.kozak.mybookshop.service.order;

import com.kozak.mybookshop.dto.order.OrderDto;
import com.kozak.mybookshop.dto.order.OrderRequestDto;
import com.kozak.mybookshop.dto.order.OrderStatusRequestDto;
import com.kozak.mybookshop.dto.orderitem.OrderItemDto;
import com.kozak.mybookshop.exception.EntityNotFoundException;
import com.kozak.mybookshop.exception.OrderProcessingException;
import com.kozak.mybookshop.mapper.OrderItemMapper;
import com.kozak.mybookshop.mapper.OrderMapper;
import com.kozak.mybookshop.model.CartItem;
import com.kozak.mybookshop.model.Order;
import com.kozak.mybookshop.model.OrderItem;
import com.kozak.mybookshop.model.ShoppingCart;
import com.kozak.mybookshop.model.User;
import com.kozak.mybookshop.repository.order.OrderRepository;
import com.kozak.mybookshop.repository.shoppingcart.ShoppingCartRepository;
import com.kozak.mybookshop.security.AuthenticationService;
import com.kozak.mybookshop.service.shoppingcart.ShoppingCartService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AuthenticationService authenticationService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartService shoppingCartService;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderDto createOrder(OrderRequestDto requestDto) {
        User currentUser = getCurrentUser();

        Set<CartItem> cartItems = getCartItemsOrThrow(currentUser);
        BigDecimal total = calculateTotal(cartItems);

        Order order = createOrderEntity(requestDto, currentUser, total);

        Set<OrderItem> orderItems = convertCartToOrderItems(cartItems, order);
        order.setOrderItems(orderItems);
        orderRepository.save(order);
        shoppingCartService.deleteAllCartItems();
        return orderMapper.toDto(order);
    }

    private User getCurrentUser() {
        return authenticationService.getCurrentUser();
    }

    private Set<CartItem> getCartItemsOrThrow(User user) {
        ShoppingCart shoppingCart =
                shoppingCartRepository.findShoppingCartByUser_Id(user.getId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "ShoppingCart not found by user id: "
                                        + user.getId()));
        Set<CartItem> cartItems = new HashSet<>(shoppingCart.getCartItems());
        if (cartItems.isEmpty()) {
            throw new OrderProcessingException("No cart items found by the userId"
                    + user.getId());
        }
        return cartItems;
    }

    private BigDecimal calculateTotal(Set<CartItem> cartItems) {
        return cartItems.stream().map(item -> BigDecimal.valueOf(
                        item.getQuantity()).multiply(item.getBook().getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order createOrderEntity(OrderRequestDto requestDto, User user, BigDecimal total) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING);
        order.setShippingAddress(requestDto.getShippingAddress());
        order.setTotal(total);
        return order;
    }

    private Set<OrderItem> convertCartToOrderItems(Set<CartItem> cartItems, Order order) {
        Set<OrderItem> orderItems = new HashSet<>();

        cartItems.forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        });
        return orderItems;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        User currentUser = authenticationService.getCurrentUser();
        return orderRepository.findAllByUser_id(currentUser.getId()).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderDto updateStatus(Long id, OrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Order not found by id: " + id));
        order.setStatus(Order.Status.valueOf(requestDto.getStatus()));
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderItemDto> getOrderItems(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Order not found by id: " + id));
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItemById(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException("Order not found by id: " + orderId));
        return order.getOrderItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .map(orderItemMapper::toDto)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order item not found by id: " + itemId));
    }
}
