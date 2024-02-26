package com.spring.eStore.service;

import com.spring.eStore.dto.AddItemToCartRequest;
import com.spring.eStore.dto.CartDto;
import com.spring.eStore.entity.CartItem;
import com.spring.eStore.entity.User;

public interface CartService {
    //add items to cart;
    //cart for user is not available then create add product
    // cart available then directly add it to cart
    CartDto addItemtoCart(String userId, AddItemToCartRequest request);
    void removeItemFromCart(String UserId, int cartItemId);
    void clearCart(String userId);
    CartDto getUserCart(String userId);
}
