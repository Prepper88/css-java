package org.uwindsor.comp8117.cssjava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uwindsor.comp8117.cssjava.dto.Session;
import org.uwindsor.comp8117.cssjava.dto.SessionView;
import org.uwindsor.comp8117.cssjava.enums.UserType;
import org.uwindsor.comp8117.cssjava.service.CustomerService;
import org.uwindsor.comp8117.cssjava.service.MessageService;
import org.uwindsor.comp8117.cssjava.service.SessionService;

import java.util.List;

@RestController
@RequestMapping("/api/session")
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @Autowired
    private MessageService messageService;

    private final String WELCOME_MESSAGE = "Welcome to our service, how can I help you?";

    @PostMapping("/loadOrCreate")
    public ResponseEntity<SessionView> loadOrCreateSession(@RequestParam Long customerId) {
        SessionView session = sessionService.loadOrCreateSession(customerId);

        if (session.getMessages().isEmpty()) {
            messageService.pushRobotMessage(session.getSessionId(), UserType.CUSTOMER, customerId, WELCOME_MESSAGE);
        }

        return ResponseEntity.ok(session);
    }


    @PostMapping("/loadSessionForAgent")
    public ResponseEntity<List<SessionView>> loadSessionForAgent(@RequestParam Long agentId) {
        List<SessionView> sessions = sessionService.loadSessionForAgent(agentId);
        return ResponseEntity.ok(sessions);
    }

    @PostMapping("/end")
    public ResponseEntity<Void> endSession(@RequestParam String sessionId, @RequestParam String endedBy, @RequestParam Long endedById) {
        sessionService.endSession(sessionId, endedBy, endedById);
        return ResponseEntity.ok().build();
    }

    // 根据 customerId 查询 active 状态的会话
    @GetMapping("/customer/{customerId}")
    public SessionView getActiveSessionByCustomerId(@PathVariable Long customerId) {
        return sessionService.getActiveSessionByCustomerId(customerId);
    }

    // 根据 agentId 查询 active 状态的会话列表
    @GetMapping("/agent/{agentId}")
    public List<SessionView> getActiveSessionsByAgentId(@PathVariable Long agentId) {
        return sessionService.getActiveSessionsByAgentId(agentId);
    }
}
