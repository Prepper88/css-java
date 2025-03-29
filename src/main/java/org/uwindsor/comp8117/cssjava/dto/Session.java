package org.uwindsor.comp8117.cssjava.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Session {
    @Id
    private String sessionId;
    private Long customerId;
    private Long agentId;
    private LocalDateTime createdAt;
    private String status;
    private String endedBy;
    private Long endedById;
    private String orderId;
    private Long ticketId;
}