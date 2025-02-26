package org.uwindsor.comp8117.cssjava.enums;

import lombok.Getter;

@Getter
public enum AgentStatus {
    AVAILABLE("available"),
    BUSY("busy"),
    OFFLINE("offline");

    private final String value;

    AgentStatus(String value) {
        this.value = value;
    }

}
