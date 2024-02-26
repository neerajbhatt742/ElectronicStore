package com.spring.eStore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {
    @NotBlank(message = "cart Id is required!")
    private String cartId;
    @NotBlank(message = "User Id is required!")
    private String userId;
    //pending,dispatched,delivered
    private String orderStatus  = "PENDING";
    //paid, not paid
    private String paymentStatus = "NOT_PAID";
    @NotBlank(message = "Address required!")
    private String billingAddress;
    @NotBlank(message = "phone number required!")
    private String billingPhone;
    @NotBlank(message = "Billing name required!")
    private String billingName;

}
