package com.spring.eStore.controller;

import com.spring.eStore.dto.AddItemToCartRequest;
import com.spring.eStore.dto.ApiResponseMessage;
import com.spring.eStore.dto.CartDto;
import com.spring.eStore.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@SecurityRequirement(name="scheme1")
@Tag(name="Cart Controller", description = "This Cart Api for cart operation")
public class CartController {

    @Autowired
    CartService cartService;
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId, @RequestBody AddItemToCartRequest request) {
        CartDto cartDto = cartService.addItemtoCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable String userId) {
        CartDto cartDto = cartService.getUserCart(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
    @DeleteMapping("{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable Integer itemId, @PathVariable String userId) {
        cartService.removeItemFromCart(userId,itemId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("item removed!!").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> removeAllItems(@PathVariable String userId) {
        cartService.clearCart(userId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Cart Cleared").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

}
