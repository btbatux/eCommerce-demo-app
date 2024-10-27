package com.btbatux.dream_shops.service.order;

import com.btbatux.dream_shops.dto.OrderDto;
import com.btbatux.dream_shops.enums.OrderStatus;
import com.btbatux.dream_shops.model.Cart;
import com.btbatux.dream_shops.model.Order;
import com.btbatux.dream_shops.model.OrderItem;
import com.btbatux.dream_shops.model.Product;
import com.btbatux.dream_shops.repository.OrderRepository;
import com.btbatux.dream_shops.repository.ProductRepository;
import com.btbatux.dream_shops.service.cart.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        CartService cartService,
                        ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.modelMapper = modelMapper;
    }


    //Siparişi Getir.
    @Override
    public OrderDto getOrder(Long orderId) {
        return convertOrderDto
                (orderRepository.findById(orderId).orElseThrow(()
                        -> new RuntimeException("No Order Found")));
    }


    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        return convertOrderDtoList
                (orderRepository.findByUserId(userId));
    }


    //Siparişi Verir. 1
    @Transactional
    @Override
    public OrderDto placeOrder(Long userId) {
        Cart userCart = cartService.getCartByUserId(userId);
        Order order = createOrder(userCart);
        List<OrderItem> orderItemsList = createOrderItems(order, userCart);
        order.setOrderItems(new HashSet<>(orderItemsList));

        order.setTotalAmount(calculateTotalAmount(orderItemsList));
        Order savedOrder = orderRepository.save(order);


        cartService.clearCart(userCart.getId());
        return convertOrderDto(savedOrder);
    }


    //Siparişi Oluşturur. 2
    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order; //oluşturulan siparişi dön.
    }


    //Sipariş Öğelerini Oluşturur. 3
    private List<OrderItem> createOrderItems(Order order, Cart userCart) {

        return userCart.getCartItems().stream().map(cartItem -> {
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


    private OrderDto convertOrderDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }

    private List<OrderDto> convertOrderDtoList(List<Order> orderList) {
        return orderList.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }
}
