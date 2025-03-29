package org.uwindsor.comp8117.cssjava.enums;

import lombok.Getter;

@Getter
public enum TicketStatus {
    CREATED("created"),
    ISSUE_CLARIFIED("issue_clarified"),
    SOLUTION_CONFIRMED("solution_confirmed"),
    EXECUTED("executed"),
    RESOLVED("resolved"),
    FEEDBACK_GIVEN("feedback_given"),
    ;

    private final String value;

    TicketStatus(String value) {
        this.value = value;
    }

}
