package org.uwindsor.comp8117.cssjava.enums;

import lombok.Getter;

@Getter
public enum UserType {
    AGENT("agent"),
    CUSTOMER("customer"),
    SYSTEM("system"),
    ;

    String value;

    UserType(String value) {
        this.value = value;
    }
}
