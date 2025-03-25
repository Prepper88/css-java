package org.uwindsor.comp8117.cssjava.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8117.cssjava.dto.Customer;
import org.uwindsor.comp8117.cssjava.dto.Message;
import org.uwindsor.comp8117.cssjava.dto.SendMessageRequest;
import org.uwindsor.comp8117.cssjava.dto.Session;
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
    @Lazy
    private TransferService transferService;

    @Autowired
    private RobotService robotService;

    @Autowired
    private NodePushService nodePushService;

    @Autowired
    private CustomerRepository customerRepository;

    // -1 represents system, 0 represents robot
    private final long SYSTEM_ID = -1L;
    private final long ROBOT_ID = 0L;
    @Autowired
    private SessionService sessionService;

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
        UserType senderType = UserType.getUserType(request.getSenderType());

        if (UserType.AGENT == senderType) {
            receiverId = session.getCustomerId();
            receiverType = UserType.CUSTOMER;
        } else if (UserType.CUSTOMER == senderType) {
            if (session.getStatus().equals(SessionStatus.SYSTEM_PROCESSING.getValue())) {
                receiverId = 0;
                receiverType = UserType.SYSTEM;
            } else {
                receiverId = session.getAgentId();
                receiverType = UserType.AGENT;
            }
        }
        if (receiverType == UserType.SYSTEM) {
            boolean isCommand = handleSystemCommands(session.getCustomerId(), sessionId, request.getMessage());
            if (!isCommand) {
                handleRobotMessage(session.getCustomerId(), sessionId, request.getMessage());
            }
        } else {
            pushMessage(sessionId, senderType, request.getSenderId(), receiverType, receiverId, message.getMessage());
        }
    }

    private void handleRobotMessage(Long customerId, String sessionId, String message) {
        log.info("Handling robot message: {}, customerId: {}, sessionId: {}", message, customerId, sessionId);
        String response = robotService.handleMessage(sessionId, customerId, message);
        saveMessage(sessionId, UserType.ROBOT, 0L, response);
    }

    private void saveMessage(String sessionId, UserType senderType, long senderId, String message) {
        Message saveMessage = new Message();
        saveMessage.setSessionId(sessionId);
        saveMessage.setSenderId(senderId);
        saveMessage.setSenderType(senderType.getValue());
        saveMessage.setMessage(message);
        saveMessage.setCreatedAt(LocalDateTime.now());
        saveMessage.setUpdatedAt(LocalDateTime.now());
        messageRepository.save(saveMessage);
    }

    public void pushMessage(String sessionId, UserType senderType, long senderId, UserType receiverType, long receiverId, String message) {
        log.info("Pushing message from {} {} to {} {}: {}", senderType, senderId, receiverType, receiverId, message);

        if (receiverType == UserType.AGENT) {
            nodePushService.sendMessageToAgent(sessionId, senderId, senderType.getValue(), receiverId, message);
        } else if (receiverType == UserType.CUSTOMER) {
            nodePushService.sendMessageToCustomer(sessionId, senderId, senderType.getValue(), receiverId, message);
        } else {
            log.warn("Unknown receiver type: {}", receiverType);
            return;
        }
        saveMessage(sessionId, senderType, senderId, message);
    }

    public void pushSystemMessage(String sessionId, UserType receiverType, long receiverId, String message) {
        pushMessage(sessionId, UserType.SYSTEM, SYSTEM_ID, receiverType, receiverId, message);
    }

    public void pushRobotMessage(String sessionId, UserType receiverType, long receiverId, String message) {
        pushMessage(sessionId, UserType.ROBOT, ROBOT_ID, receiverType, receiverId, message);
    }

    private boolean handleSystemCommands(long customerId, String sessionId, String message) {
        log.info("Handling system command: {}, customerId: {}, sessionId: {}", message, customerId, sessionId);
        return switch (message) {
            case "transfer to human", "T2H", "transfer to agent", "T2A", "zrg", "Live agent please", "LAP" -> {
                transferService.transferToAgent(sessionId);
                yield true;
            }
            case "end session", "end", "close", "bye", "goodbye" -> {
                sessionService.endSession(sessionId, "customer", customerId);
                yield true;
            }
            default -> {
                log.warn("Unknown system command: {}", message);
                yield false;
            }
        };
    }
}
