package com.spring.eStore.dto;

import com.spring.eStore.entity.OrderItem;
import com.spring.eStore.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private String orderId;
    //pending,dispatched,delivered
    private String orderStatus  = "PENDING";
    //paid, not paid
    private String paymentStatus = "NOT_PAID";
    private int orderAmount;
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderedDate = new Date();
    private Date deliveredDate;
//    private UserDto userdto;
    private List<OrderItemDto> orderItems = new ArrayList<>();
}
