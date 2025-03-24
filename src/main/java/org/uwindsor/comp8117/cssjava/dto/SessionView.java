package org.uwindsor.comp8117.cssjava.dto;

import lombok.Data;

import java.util.List;

/**
 * session view for ui display purpose
 */
@Data
public class SessionView {
    private String sessionId;
    private Long agentId;
    private Long customerId;
    private String customerName;
    private String status;
    private List<Message> messages;
}
