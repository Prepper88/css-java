package org.uwindsor.comp8117.cssjava.dto;

import lombok.Data;

@Data
public class Ticket {
    private String issueType;
    private String issue;
    private String remark;
    private String userRequest;
    private String confirmedSolution;
    private String status;
}
