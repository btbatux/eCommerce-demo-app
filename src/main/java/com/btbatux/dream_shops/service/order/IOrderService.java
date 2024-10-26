package com.btbatux.dream_shops.service.order;

import com.btbatux.dream_shops.dto.OrderDto;
import com.btbatux.dream_shops.model.Order;

import java.util.List;

public interface IOrderService {


    OrderDto getOrder(Long OrderId);

    List<OrderDto> getUserOrders(Long userId);

    //Sipariş ver
    OrderDto placeOrder(Long userId);
}
