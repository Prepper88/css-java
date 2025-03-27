package org.uwindsor.comp8117.cssjava.dto;

import lombok.Data;

@Data
public class SendMessageRequest {
    private String sessionId;
    private Long senderId;
    private String senderType;
    private String content;
    private String messageType;
}
