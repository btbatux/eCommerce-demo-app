package com.btbatux.dream_shops.repository;

import com.btbatux.dream_shops.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
