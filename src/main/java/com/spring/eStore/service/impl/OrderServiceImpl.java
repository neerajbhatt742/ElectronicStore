package com.spring.eStore.service.impl;

import com.spring.eStore.dto.CreateOrderRequest;
import com.spring.eStore.dto.OrderDto;
import com.spring.eStore.dto.PageableResponse;
import com.spring.eStore.entity.*;
import com.spring.eStore.exception.BadApiRequest;
import com.spring.eStore.exception.ResourceNotFoundException;
import com.spring.eStore.helper.Helper;
import com.spring.eStore.repository.CartRepository;
import com.spring.eStore.repository.OrderRepository;
import com.spring.eStore.repository.UserRepository;
import com.spring.eStore.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
         //fetch user
        User user = userRepository.findById(orderDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not Found"));
        //fetch cart
        Cart cart = cartRepository.findById(orderDto.getCartId()).orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));
        List<CartItem> items = cart.getItems();
        if(items.size() <= 0) {
            throw new BadApiRequest("Invalid number of items in cart");
        }
        //extra validation
        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .billingAddress(orderDto.getBillingAddress())
                .orderedDate(new Date())
                .orderedDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user).build();

        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
        List<OrderItem> orderItems = items.stream().map(cartItem -> {
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();
            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());
        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());
        cart.getItems().clear();
        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);
        OrderDto mappedOrderDto = modelMapper.map(savedOrder, OrderDto.class);
        return mappedOrderDto;
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found!!"));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersOfUsers(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found !!"));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> ordersDto = orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return ordersDto;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> page = orderRepository.findAll(pageable);
        PageableResponse<OrderDto> pageableResponse = Helper.getPageableResponse(page, OrderDto.class);
        return pageableResponse;
    }
}
