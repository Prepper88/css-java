package org.uwindsor.comp8117.cssjava.enums;

import lombok.Getter;

@Getter
public enum SessionStatus {
    ROBOT_PROCESSING("system_processing"),
    AGENT_PROCESSING("agent_processing"),
    ENDED("ended");

    private final String value;

    SessionStatus(String value) {
        this.value = value;
    }

}
