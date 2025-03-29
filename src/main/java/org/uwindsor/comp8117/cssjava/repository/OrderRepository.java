package org.uwindsor.comp8117.cssjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uwindsor.comp8117.cssjava.dto.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(long id);
    Order findByOrderId(String id);
    List<Order> findByCustomerId(Long customerId);
}