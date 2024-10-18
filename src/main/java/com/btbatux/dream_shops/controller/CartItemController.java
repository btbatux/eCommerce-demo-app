package com.btbatux.dream_shops.controller;

import com.btbatux.dream_shops.exception.ResourceNotFoundException;
import com.btbatux.dream_shops.response.ApiResponse;
import com.btbatux.dream_shops.service.cart.CartItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController // JSON veya XML gibi doğrudan veri formatlarını döndürmek için kullanılır.
@RequestMapping("/api/v1/cartItems")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }


    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long cartID,
                                                     @RequestParam Long productId,
                                                     @RequestParam Integer quantity) {
        try {
            cartItemService.addItemToCart(cartID, productId, quantity);
            return ResponseEntity.
                    ok(new ApiResponse("Add Item Success", null));
        } catch (ResourceNotFoundException r) {
            return ResponseEntity.
                    status(NOT_FOUND).
                    body(new ApiResponse(r.getMessage(), null));
        }

    }
}
