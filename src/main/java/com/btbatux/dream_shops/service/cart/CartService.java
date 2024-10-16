package com.btbatux.dream_shops.service.cart;

import com.btbatux.dream_shops.exception.ResourceNotFoundException;
import com.btbatux.dream_shops.model.Cart;
import com.btbatux.dream_shops.model.CartItem;
import com.btbatux.dream_shops.repository.CartItemRepository;
import com.btbatux.dream_shops.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }


    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id).
                orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);

    }


    @Override
    public void clearCart(Long id) {

        Cart cart = getCart(id);

        cartItemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cartRepository.deleteById(id);
    }


    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }
}
