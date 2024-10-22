package com.btbatux.dream_shops.service.order;

import com.btbatux.dream_shops.model.Cart;
import com.btbatux.dream_shops.model.Order;
import com.btbatux.dream_shops.model.OrderItem;
import com.btbatux.dream_shops.model.Product;
import com.btbatux.dream_shops.repository.OrderRepository;
import com.btbatux.dream_shops.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }


    //Siparişi Getir.
    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).
                orElseThrow(()
                        -> new RuntimeException("Order not found"));
    }


    //Sipariş Ver.
    @Override
    public Order placeOrder(Order order) {
        return null;
    }


    //Sipariş Öğeleri Oluşturur.
    private List<OrderItem> createOrderItems(Order order, Cart cart) {

        return cart.getCartItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(order, product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice());
        }).toList();
    }



    private BigDecimal calculateTotalAmount(List<OrderItem> orderList) {
        return orderList.stream().
                map(item ->
                        item.getPrice().multiply(new BigDecimal(item.getQuantity()))).
                reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}
