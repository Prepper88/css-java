package org.uwindsor.comp8117.cssjava.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8117.cssjava.dto.Customer;
import org.uwindsor.comp8117.cssjava.dto.Message;
import org.uwindsor.comp8117.cssjava.dto.SendMessageRequest;
import org.uwindsor.comp8117.cssjava.dto.Session;
import org.uwindsor.comp8117.cssjava.enums.UserType;
import org.uwindsor.comp8117.cssjava.repository.CustomerRepository;
import org.uwindsor.comp8117.cssjava.repository.MessageRepository;
import org.uwindsor.comp8117.cssjava.repository.SessionRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TransferService transferService;

    @Autowired
    private NodePushService nodePushService;

    @Autowired
    private CustomerRepository customerRepository;

    public void sendMessage(SendMessageRequest request) {
        String sessionId = request.getSessionId();
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));
        Message message = new Message();
        message.setSessionId(sessionId);
        message.setSenderId(request.getSenderId());
        message.setSenderType(request.getSenderType());
        message.setMessage(request.getMessage());
        message.setCreatedAt(LocalDateTime.now());
        message.setUpdatedAt(LocalDateTime.now());
        messageRepository.save(message);

        // Push message to agent or customer
        long receiverId = -1;
        UserType receiverType = null;
        if (UserType.AGENT.getValue().equals(request.getSenderType())) {
            receiverId = session.getCustomerId();
            receiverType = UserType.CUSTOMER;
        } else if (UserType.CUSTOMER.getValue().equals(request.getSenderType())) {
            receiverId = session.getAgentId();
            if (receiverId == 0) {
                receiverType = UserType.SYSTEM;
            } else {
                receiverType = UserType.AGENT;
            }
        }
        // TODO: remove agent
        if (receiverType == UserType.SYSTEM) {
            handleSystemCommands(session.getCustomerId(), sessionId, request.getMessage());
        } else {
            pushMessage(sessionId, request.getSenderId(), receiverType, receiverId, message.getMessage());
        }
    }

    private void pushMessage(String sessionId, long senderId, UserType receiverType, long receiverId, String message) {
        log.info("Pushing message from {} to {}: {}, receiver type: {}", senderId, receiverId, message, receiverType);

        if (receiverType == UserType.AGENT) {
            Customer customer = customerRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Customer not found"));
            nodePushService.sendMessageToAgent(sessionId, senderId, customer.getUsername(), receiverId, message);
        } else if (receiverType == UserType.CUSTOMER) {
            nodePushService.sendMessageToCustomer(sessionId, senderId, "agent", receiverId, message);
        } else {
            log.warn("Unknown receiver type: {}", receiverType);
        }
    }

    private void handleSystemCommands(long customerId, String sessionId, String message) {
        log.info("Handling system command: {}, customerId: {}, sessionId: {}", message, customerId, sessionId);
        switch (message) {
            case "transfer to human":
            case "T2H":
            case "transfer to agent":
            case "T2A":
            case "zrg":
            case "Live agent please":
            case "LAP":
                transferService.transferToAgent(sessionId);
                break;
            default:
                log.warn("Unknown system command: {}", message);
        }
    }
}
