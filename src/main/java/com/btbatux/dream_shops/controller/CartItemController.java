package com.btbatux.dream_shops.controller;

import com.btbatux.dream_shops.exception.ResourceNotFoundException;
import com.btbatux.dream_shops.response.ApiResponse;
import com.btbatux.dream_shops.service.cart.CartItemService;
import com.btbatux.dream_shops.service.cart.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController // JSON veya XML gibi doğrudan veri formatlarını döndürmek için kullanılır.
@RequestMapping("/api/v1/cartItems")
public class CartItemController {

    private final CartItemService cartItemService;
    private final CartService cartService;

    public CartItemController(CartItemService cartItemService, CartService cartService) {
        this.cartItemService = cartItemService;
        this.cartService = cartService;
    }


    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam(required = false) Long cartID,
                                                     @RequestParam Long productId,
                                                     @RequestParam Integer quantity) {
        try {
            if (cartID == null) {
                cartID = cartService.initializeNewCart();
            }

            cartItemService.addItemToCart(cartID, productId, quantity);
            return ResponseEntity.
                    ok(new ApiResponse("Add Item Success", null));
        } catch (ResourceNotFoundException r) {
            return ResponseEntity.
                    status(NOT_FOUND).
                    body(new ApiResponse(r.getMessage(), null));
        }
    }

    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId,
                                                          @PathVariable Long itemId) {
        try {
            cartItemService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.
                    ok(new ApiResponse("Remove Item Success", null));
        } catch (ResourceNotFoundException r) {
            return ResponseEntity.
                    status(NOT_FOUND).
                    body(new ApiResponse(r.getMessage(), null));
        }
    }

    @PutMapping("/cart{cartId}/item{productId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                          @PathVariable Long productId,
                                                          @RequestParam Integer quantity) {
        cartItemService.updateItemQuantity(cartId, productId, quantity);
        try {
            return ResponseEntity.
                    ok(new ApiResponse("Update Item Success", null));
        } catch (ResourceNotFoundException r) {
            return ResponseEntity.
                    status(NOT_FOUND).
                    body(new ApiResponse(r.getMessage(), null));
        }
    }
}
