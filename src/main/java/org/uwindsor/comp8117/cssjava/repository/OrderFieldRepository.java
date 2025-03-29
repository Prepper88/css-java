package org.uwindsor.comp8117.cssjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uwindsor.comp8117.cssjava.dto.OrderField;

import java.util.List;

public interface OrderFieldRepository extends JpaRepository<OrderField, String> {
    List<OrderField> findOrderFieldByOrderId(String orderId);
}
