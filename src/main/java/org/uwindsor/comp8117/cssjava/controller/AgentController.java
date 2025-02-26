package org.uwindsor.comp8117.cssjava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uwindsor.comp8117.cssjava.dto.Agent;
import org.uwindsor.comp8117.cssjava.dto.LoginRequest;
import org.uwindsor.comp8117.cssjava.enums.AgentStatus;
import org.uwindsor.comp8117.cssjava.service.AgentService;

@RestController
@RequestMapping("/api/agent")
public class AgentController {
    @Autowired
    private AgentService agentService;

    @PostMapping("/login")
    public ResponseEntity<Agent> login(@RequestBody LoginRequest loginRequest) {
        Agent agent = agentService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (agent != null) {
            // initially set offline, switch to online when socket connection is established
            agentService.updateStatus(agent.getId(), AgentStatus.OFFLINE.getValue());
            agent.setPassword(null);
            return ResponseEntity.ok(agent);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/update-status")
    public ResponseEntity<Void> updateStatus(@RequestParam Long agentId, @RequestParam String status) {
        agentService.updateStatus(agentId, status);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update-info")
    public ResponseEntity<Void> updateAgentInfo(@RequestParam Long agentId, @RequestParam(required = false) String password,
                                                @RequestParam(required = false) String status, @RequestParam(required = false) int maxCustomers) {
        agentService.updateAgentInfo(agentId, password, status, maxCustomers);
        return ResponseEntity.ok().build();
    }
}