package com.btbatux.dream_shops.service.order;

import com.btbatux.dream_shops.model.Order;

import java.util.List;

public interface IOrderService {


    Order getOrder(Long OrderId);

    List<Order> getUserOrders(Long userId);

    //Sipariş ver
    Order placeOrder(Long userId);
}
