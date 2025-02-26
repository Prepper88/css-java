package org.uwindsor.comp8117.cssjava.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sessionId;
    private Long senderId;
    private String senderType;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}