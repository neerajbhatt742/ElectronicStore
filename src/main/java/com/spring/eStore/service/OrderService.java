package com.spring.eStore.service;

import com.spring.eStore.dto.CreateOrderRequest;
import com.spring.eStore.dto.OrderDto;
import com.spring.eStore.dto.PageableResponse;

import java.util.List;

public interface OrderService {
    //create order
    OrderDto createOrder(CreateOrderRequest orderDto);
    //remove order
    void removeOrder(String orderId);

    //get orders of users
    List<OrderDto> getOrdersOfUsers(String userId);

    //get orders
    PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);
    //other methods related to order
}
