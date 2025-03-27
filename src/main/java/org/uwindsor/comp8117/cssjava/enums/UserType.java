package org.uwindsor.comp8117.cssjava.enums;

import lombok.Getter;

@Getter
public enum UserType {
    AGENT("agent"),
    CUSTOMER("customer"),
    ROBOT("robot"),
    ;

    String value;

    UserType(String value) {
        this.value = value;
    }

    public static UserType getUserType(String value) {
        for (UserType userType : UserType.values()) {
            if (userType.value.equals(value)) {
                return userType;
            }
        }
        return null;
    }
}
