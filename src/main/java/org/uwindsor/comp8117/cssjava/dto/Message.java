package org.uwindsor.comp8117.cssjava.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sessionId;
    private Long senderId;
    private String senderType;
    private String content;
    private String messageType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}