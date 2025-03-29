package org.uwindsor.comp8117.cssjava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uwindsor.comp8117.cssjava.dto.SendMessageRequest;
import org.uwindsor.comp8117.cssjava.service.MessageService;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody SendMessageRequest request) {
        messageService.sendMessage(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/send-service-progress-card")
    public ResponseEntity<Void> sendServiceProgressCard(@RequestParam String sessionId) {
        messageService.sendServiceProgressCard(sessionId);
        return ResponseEntity.ok().build();
    }
}
