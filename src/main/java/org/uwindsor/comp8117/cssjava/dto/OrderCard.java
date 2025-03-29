package org.uwindsor.comp8117.cssjava.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCard {
    private Long id;
    private String orderId;
    private String orderTitle;
    private LocalDateTime createdAt;
    private String payStatus;
    private String deliveryStatus;
    private String price;
    private String imageName;
}
