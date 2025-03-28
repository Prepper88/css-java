package org.uwindsor.comp8117.cssjava.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8117.cssjava.dto.Message;
import org.uwindsor.comp8117.cssjava.dto.SessionView;
import org.uwindsor.comp8117.cssjava.util.HttpUtil;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class NodePushService {

    private static final String AGENT_PUSH_MESSAGE_URL = "http://localhost:3001/api/push-message";
    private static final String CUSTOMER_PUSH_MESSAGE_URL = "http://localhost:3000/api/push-message";
    private static final String NOTIFY_NEW_SESSION_URL = "http://localhost:3001/api/notify-new-session";

    // Send a message to an agent
    public void sendMessageToAgent(Long agentId, Message message) {
        Map<String, Object> body = new HashMap<>();
        body.put("sessionId", message.getSessionId());
        body.put("senderId", message.getSenderId());
        body.put("senderType", message.getSenderType());
        body.put("agentId", agentId);
        body.put("messageType", message.getMessageType());
        body.put("content", message.getContent());

        try {
            ResponseEntity<String> response = HttpUtil.post(AGENT_PUSH_MESSAGE_URL, body);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.warn("Failed to send message to agent: {}", response.getBody());
            }
        } catch (Exception e) {
            log.warn("Failed to send message to agent: {}", e.getMessage());
        }
    }

    // Send a message to a customer
    public void sendMessageToCustomer(Long customerId, Message message) {
        Map<String, Object> body = new HashMap<>();
        body.put("sessionId", message.getSessionId());
        body.put("senderId", message.getSenderId());
        body.put("senderType", message.getSenderType());
        body.put("customerId", customerId);
        body.put("messageType", message.getMessageType());
        body.put("content", message.getContent());

        try {
            ResponseEntity<String> response = HttpUtil.post(CUSTOMER_PUSH_MESSAGE_URL, body);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to send message to customer: " + response.getBody());
            }
        } catch (Exception e) {
            log.warn("Failed to send message to agent: {}", e.getMessage());
        }
    }

    // Notify an agent about a new session
    public void notifyNewSession(SessionView sessionView) {
        try {
            ResponseEntity<String> response = HttpUtil.post(NOTIFY_NEW_SESSION_URL, sessionView);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to notify agent about new session: " + response.getBody());
            }
        } catch (Exception e) {
            log.warn("Failed to send message to agent: {}", e.getMessage());
        }
    }
}