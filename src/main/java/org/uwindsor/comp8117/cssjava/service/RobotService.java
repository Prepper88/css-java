package org.uwindsor.comp8117.cssjava.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8117.cssjava.dto.Message;
import org.uwindsor.comp8117.cssjava.dto.Session;
import org.uwindsor.comp8117.cssjava.enums.MessageType;
import org.uwindsor.comp8117.cssjava.enums.UserType;
import org.uwindsor.comp8117.cssjava.repository.MessageRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
public class RobotService {
    @Autowired
    private NodePushService nodePushService;

    @Autowired
    private TransferService transferService;

    @Autowired
    private SessionService sessionService;

    // -1 represents system, 0 represents robot
    private final long ROBOT_ID = 0L;

    private final String WELCOME_MESSAGE = "Welcome to our service, how can I help you?";
    @Autowired
    private MessageRepository messageRepository;

    public Message handleMessage(Message message) {
        if (handleSystemCommands(message)) {
            return null;
        }
        Message response = getResponse(message);
        nodePushService.sendMessageToCustomer(message.getSenderId(), response);
        return response;
    }

    private Message getResponse(Message message) {
        String responseContent = switch (message.getContent()) {
            case "Hi", "Hi!", "Hello" -> "Hello!";
            default -> "Sorry, I can't understand you.";
        };
        return Message.builder()
                .sessionId(message.getSessionId())
                .senderId(ROBOT_ID)
                .senderType(UserType.ROBOT.getValue())
                .messageType(MessageType.TEXT.getValue())
                .content(responseContent)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Message pushWelcomeMessage(Session session, long customerId) {
        Message message = Message.builder()
                .sessionId(session.getSessionId())
                .senderId(ROBOT_ID)
                .senderType(UserType.ROBOT.getValue())
                .messageType(MessageType.TEXT.getValue())
                .content(WELCOME_MESSAGE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        messageRepository.save(message);
        nodePushService.sendMessageToCustomer(customerId, message);
        return message;
    }

    private boolean handleSystemCommands(Message message) {
        String sessionId = message.getSessionId();
        long customerId = message.getSenderId();
        String content = message.getContent();
        log.info("Handling system command: {}, customerId: {}, sessionId: {}", message, customerId, sessionId);
        return switch (content) {
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
