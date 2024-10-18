package com.btbatux.dream_shops.controller;

import com.btbatux.dream_shops.exception.ResourceNotFoundException;
import com.btbatux.dream_shops.model.Cart;
import com.btbatux.dream_shops.response.ApiResponse;
import com.btbatux.dream_shops.service.cart.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;


@RestController // JSON veya XML gibi doğrudan veri formatlarını döndürmek için kullanılır.
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @GetMapping("/{cartId}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {
        try {
            Cart cart = cartService.getCart(cartId);
            return ResponseEntity.
                    ok(new ApiResponse("Success", cart));
        } catch (ResourceNotFoundException r) {
            return ResponseEntity.
                    status(NOT_FOUND).
                    body(new ApiResponse("Not Found", null));
        }

    }


    @DeleteMapping("{cartId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
        try {
            cartService.clearCart(cartId);
            return ResponseEntity.
                    ok(new ApiResponse("Success", null));
        } catch (ResourceNotFoundException r) {
            return ResponseEntity.
                    status(NOT_FOUND).
                    body(new ApiResponse("Not Found", null));

        }
    }


    @GetMapping("/{cartId}/cart/total-price")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {
        try {
            BigDecimal totalPrice = cartService.getTotalPrice(cartId);
            return ResponseEntity.
                    ok(new ApiResponse("Success", totalPrice));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.
                    status(NOT_FOUND).
                    body(new ApiResponse(e.getMessage(),null));
        }

    }
}
