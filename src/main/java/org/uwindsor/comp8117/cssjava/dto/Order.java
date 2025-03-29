package org.uwindsor.comp8117.cssjava.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderId;
    private String orderTitle;
    private LocalDateTime createdAt;
    private String payStatus;
    private String deliveryStatus;
    private String price;
    private String imageName;
    private Long customerId;
}
