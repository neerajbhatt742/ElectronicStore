package com.spring.eStore.repository;

import com.spring.eStore.entity.Cart;
import com.spring.eStore.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
}
