package org.uwindsor.comp8117.cssjava.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8117.cssjava.dto.SessionView;
import org.uwindsor.comp8117.cssjava.util.HttpUtil;

import java.util.HashMap;
import java.util.Map;

@Service
public class NodePushService {

    private static final String AGENT_PUSH_MESSAGE_URL = "http://localhost:3001/api/push-message";
    private static final String CUSTOMER_PUSH_MESSAGE_URL = "http://localhost:3000/api/push-message";
    private static final String NOTIFY_NEW_SESSION_URL = "http://localhost:3001/api/notify-new-session";

    // Send a message to an agent
    public void sendMessageToAgent(String sessionId, Long sendId, String sendName, Long agentId, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("sessionId", sessionId);
        body.put("sendId", sendId);
        body.put("sendName", sendName);
        body.put("agentId", agentId);
        body.put("message", message);

        ResponseEntity<String> response = HttpUtil.post(AGENT_PUSH_MESSAGE_URL, body);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send message to agent: " + response.getBody());
        }
    }

    // Send a message to a customer
    public void sendMessageToCustomer(String sessionId, Long sendId, String sendName, Long customerId, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("sessionId", sessionId);
        body.put("sendId", sendId);
        body.put("sendName", sendName);
        body.put("customerId", customerId);
        body.put("message", message);

        ResponseEntity<String> response = HttpUtil.post(CUSTOMER_PUSH_MESSAGE_URL, body);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send message to customer: " + response.getBody());
        }
    }

    // Notify an agent about a new session
    public void notifyNewSession(SessionView sessionView) {

        ResponseEntity<String> response = HttpUtil.post(NOTIFY_NEW_SESSION_URL, sessionView);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to notify agent about new session: " + response.getBody());
        }
    }
}