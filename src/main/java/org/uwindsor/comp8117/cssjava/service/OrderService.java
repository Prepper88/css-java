package org.uwindsor.comp8117.cssjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8117.cssjava.dto.Order;
import org.uwindsor.comp8117.cssjava.dto.OrderCard;
import org.uwindsor.comp8117.cssjava.repository.OrderRepository;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<OrderCard> findOrderCardsByCustomerId(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        return orders.stream()
                .map(this::orderToOrderCard)
                .toList();
    }

    public OrderCard findOrderCardByOrderId(String orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        return orderToOrderCard(order);
    }

    private OrderCard orderToOrderCard(Order order) {
        return OrderCard.builder()
                .id(order.getId())
                .orderId(order.getOrderId())
                .orderTitle(order.getOrderTitle())
                .createdAt(order.getCreatedAt())
                .payStatus(order.getPayStatus())
                .deliveryStatus(order.getDeliveryStatus())
                .price(order.getPrice())
                .imageName(order.getImageName())
                .build();
    }
}
