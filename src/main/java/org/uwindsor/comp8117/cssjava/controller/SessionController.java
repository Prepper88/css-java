package org.uwindsor.comp8117.cssjava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uwindsor.comp8117.cssjava.dto.Session;
import org.uwindsor.comp8117.cssjava.dto.SessionView;
import org.uwindsor.comp8117.cssjava.service.SessionService;

import java.util.List;

@RestController
@RequestMapping("/api/session")
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @PostMapping("/create")
    public ResponseEntity<Session> createSession(@RequestParam Long customerId) {
        Session session = sessionService.createSessionIfNotExist(customerId);
        return ResponseEntity.ok(session);
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
