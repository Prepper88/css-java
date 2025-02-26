package org.uwindsor.comp8117.cssjava.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
