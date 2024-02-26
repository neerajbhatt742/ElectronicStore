package com.spring.eStore.controller;

import com.spring.eStore.dto.ApiResponseMessage;
import com.spring.eStore.dto.CreateOrderRequest;
import com.spring.eStore.dto.OrderDto;
import com.spring.eStore.dto.PageableResponse;
import com.spring.eStore.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@SecurityRequirement(name="scheme1")
public class OrderController {

    @Autowired
    OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest orderRequest) {
    OrderDto order = orderService.createOrder(orderRequest);
    return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId) {
        orderService.removeOrder(orderId);
        ApiResponseMessage orderRemoved = ApiResponseMessage.builder()
                .status(HttpStatus.OK)
                .success(true)
                .message("Order removed").build();
        return new ResponseEntity<>(orderRemoved,HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getUserOrders(@PathVariable String userId) {
        List<OrderDto> ordersOfUsers = orderService.getOrdersOfUsers(userId);
        return new ResponseEntity<>(ordersOfUsers,HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getUserOrders(
            @RequestParam(value="pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value="pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value="sortBy", defaultValue = "orderedDate", required = false) String sortBy,
            @RequestParam(value="sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<OrderDto> orders = orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);

        return new ResponseEntity<>(orders,HttpStatus.OK);
    }
}
