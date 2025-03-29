package org.uwindsor.comp8117.cssjava.dto;

import lombok.Data;

import java.time.LocalDateTime;
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
    private OrderCard orderCard;
    private Long ticketId;
    private LocalDateTime createdAt;

    public Session toSession() {
        Session session = new Session();
        session.setSessionId(sessionId);
        session.setAgentId(agentId);
        session.setCustomerId(customerId);
        session.setStatus(status);
        session.setCreatedAt(createdAt);
        if (orderCard != null) {
            session.setOrderId(orderCard.getOrderId());
        }
        session.setTicketId(ticketId);
        return session;
    }
}
