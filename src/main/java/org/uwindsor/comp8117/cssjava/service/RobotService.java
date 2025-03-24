package org.uwindsor.comp8117.cssjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RobotService {
    @Autowired
    private NodePushService nodePushService;

    // -1 represents system, 0 represents robot
    private final long ROBOT_ID = 0L;

    public String handleMessage(String sessionId, Long senderId, String message) {
        String response = getResponse(message);
        nodePushService.sendMessageToCustomer(sessionId, ROBOT_ID, "robot", senderId, response);
        return response;
    }

    private String getResponse(String message) {
        String response = switch (message) {
            case "Hi", "Hi!", "Hello" -> "Hello!";
            default -> "Sorry, I can't understand you.";
        };
        return response;
    }
}
