package org.uwindsor.comp8117.cssjava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.uwindsor.comp8117.cssjava.dto.Agent;
import org.uwindsor.comp8117.cssjava.enums.AgentStatus;
import org.uwindsor.comp8117.cssjava.service.AgentService;

@RestController
@RequestMapping("/api/agent")
public class AgentController {
    @Autowired
    private AgentService agentService;

    @PostMapping("/login")
    public ResponseEntity<Agent> login(@RequestParam String username, @RequestParam String password) {
        Agent agent = agentService.login(username, password);
        if (agent != null) {
            // initially set offline, switch to online when socket connection is established
            agentService.updateStatus(agent.getId(), AgentStatus.OFFLINE.getValue());
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