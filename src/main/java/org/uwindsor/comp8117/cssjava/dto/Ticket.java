package org.uwindsor.comp8117.cssjava.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String issueType;
    private String issue;
    private String remark;
    private String customerRequest;
    private String confirmedSolution;
    private String status;
    private String sessionId;
    private LocalDateTime createdAt;
}
