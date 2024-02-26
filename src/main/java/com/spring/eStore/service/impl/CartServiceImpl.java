package com.spring.eStore.service.impl;

import com.spring.eStore.dto.AddItemToCartRequest;
import com.spring.eStore.dto.CartDto;
import com.spring.eStore.entity.Cart;
import com.spring.eStore.entity.CartItem;
import com.spring.eStore.entity.Product;
import com.spring.eStore.entity.User;
import com.spring.eStore.exception.ResourceNotFoundException;
import com.spring.eStore.repository.CartItemRepository;
import com.spring.eStore.repository.CartRepository;
import com.spring.eStore.repository.ProductRepository;
import com.spring.eStore.repository.UserRepository;
import com.spring.eStore.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartDto addItemtoCart(String userId, AddItemToCartRequest request) {
        int quantity = request.getQuantity();
        String productId = request.getProductId();

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = null;
        try {
            cart = cartRepository.findByUser(user).get();
        }
        catch (NoSuchElementException ex) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }
        //if cart item already in cart then update quantity and price
        AtomicBoolean updated= new AtomicBoolean(false);

        List<CartItem> items = cart.getItems();
        items= items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getDiscountedPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());
//        cart.setItems(updatedList);
        if(!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product).build();
            cart.getItems().add(cartItem);
        }
        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return modelMapper.map(updatedCart,CartDto.class);
    }

    @Override
    public void removeItemFromCart(String UserId, int cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("item not found"));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart not found"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getUserCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart not found"));
        return modelMapper.map(cart,CartDto.class);
    }
}
