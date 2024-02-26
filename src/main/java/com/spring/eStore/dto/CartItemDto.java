package com.spring.eStore.dto;

import com.spring.eStore.entity.Cart;
import com.spring.eStore.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {
    private int cartItemId;
    private Product product;
    private int quantity;
    private int totalPrice;
}
