package org.uwindsor.comp8117.cssjava.enums;

import lombok.Getter;

@Getter
public enum MessageType {
    TEXT("text"),
    ORDER_LIST("order-list"),
    COMMAND("command"),
    ;

    String value;

    MessageType(String value) {
        this.value = value;
    }

    public static MessageType getMessageType(String value) {
        for (MessageType messageType : MessageType.values()) {
            if (messageType.value.equals(value)) {
                return messageType;
            }
        }
        return null;
    }
}
