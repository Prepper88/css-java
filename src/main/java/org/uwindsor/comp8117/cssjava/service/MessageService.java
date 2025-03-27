package org.uwindsor.comp8117.cssjava.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8117.cssjava.dto.Message;
import org.uwindsor.comp8117.cssjava.dto.SendMessageRequest;
import org.uwindsor.comp8117.cssjava.dto.Session;
import org.uwindsor.comp8117.cssjava.enums.MessageType;
import org.uwindsor.comp8117.cssjava.enums.SessionStatus;
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
    private RobotService robotService;

    @Autowired
    private NodePushService nodePushService;

    // -1 represents system, 0 represents robot
    private final long ROBOT_ID = 0L;

    public void sendMessage(SendMessageRequest request) {
        String sessionId = request.getSessionId();
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));
        Message message = Message.builder()
                .sessionId(sessionId)
                .senderId(request.getSenderId())
                .senderType(request.getSenderType())
                .content(request.getContent())
                .messageType(request.getMessageType())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        messageRepository.save(message);

        // Push message to agent or customer
        long receiverId = -1;
        UserType receiverType = null;
        UserType senderType = UserType.getUserType(request.getSenderType());

        if (UserType.AGENT == senderType) {
            receiverId = session.getCustomerId();
            receiverType = UserType.CUSTOMER;
        } else if (UserType.CUSTOMER == senderType) {
            if (session.getStatus().equals(SessionStatus.ROBOT_PROCESSING.getValue())) {
                receiverId = 0;
                receiverType = UserType.ROBOT;
            } else {
                receiverId = session.getAgentId();
                receiverType = UserType.AGENT;
            }
        }
        if (receiverType == UserType.ROBOT) {
            Message response = robotService.handleMessage(message);
            if (response != null) {
                messageRepository.save(response);
                pushMessage(receiverType, receiverId, response);
            }
        } else {
            pushMessage(receiverType, receiverId, message);
        }
    }

    public void pushMessage(UserType receiverType, long receiverId, Message message) {
        log.info("Pushing message to {} {}: {}", receiverType, receiverId, message);

        if (receiverType == UserType.AGENT) {
            nodePushService.sendMessageToAgent(receiverId, message);
        } else if (receiverType == UserType.CUSTOMER) {
            nodePushService.sendMessageToCustomer(receiverId, message);
        } else {
            log.warn("Unknown receiver type: {}", receiverType);
        }
    }

    public void pushMessageToCustomerAndAgent(long customerId, long agentId, Message message) {
        log.info("Pushing message to customer: {}, agent: {}, message: {}", customerId, agentId, message);
        messageRepository.save(message);
        nodePushService.sendMessageToAgent(agentId, message);
        nodePushService.sendMessageToCustomer(customerId, message);
    }

    public void pushRobotMessage(Session session, String content) {
        Message message = Message.builder()
                .sessionId(session.getSessionId())
                .senderType(UserType.ROBOT.getValue())
                .senderId(ROBOT_ID)
                .content(content)
                .messageType(MessageType.TEXT.getValue())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        nodePushService.sendMessageToCustomer(session.getCustomerId(), message);
    }

    public void pushSystemMessage(Session session, String content) {
        Message message = Message.builder()
                .sessionId(session.getSessionId())
                .senderType(UserType.ROBOT.getValue())
                .senderId(ROBOT_ID)
                .content(content)
                .messageType(MessageType.SYSTEM_NOTICE.getValue())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        messageRepository.save(message);
        nodePushService.sendMessageToCustomer(session.getCustomerId(), message);

        if (session.getStatus().equals(SessionStatus.AGENT_PROCESSING.getValue())) {
            nodePushService.sendMessageToAgent(session.getAgentId(), message);
        }
    }
}
