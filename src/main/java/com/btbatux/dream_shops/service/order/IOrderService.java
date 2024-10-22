package com.btbatux.dream_shops.service.order;

import com.btbatux.dream_shops.model.Order;

public interface IOrderService {

    Order placeOrder(Order order);

    Order getOrder(Long OrderId);

}
