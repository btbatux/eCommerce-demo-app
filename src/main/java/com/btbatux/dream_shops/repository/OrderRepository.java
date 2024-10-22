package com.btbatux.dream_shops.repository;

import com.btbatux.dream_shops.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
