package org.uwindsor.comp8117.cssjava.dto;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String status;
    private int maxCustomers;
}
