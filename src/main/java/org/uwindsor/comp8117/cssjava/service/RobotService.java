package org.uwindsor.comp8117.cssjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8117.cssjava.dto.Message;
import org.uwindsor.comp8117.cssjava.enums.MessageType;
import org.uwindsor.comp8117.cssjava.enums.UserType;

import java.time.LocalDateTime;

@Service
public class RobotService {
    @Autowired
    private NodePushService nodePushService;

    // -1 represents system, 0 represents robot
    private final long ROBOT_ID = 0L;

    public Message handleMessage(Message message) {
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
}
