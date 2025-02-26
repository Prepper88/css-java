package org.uwindsor.comp8117.cssjava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.uwindsor.comp8117.cssjava.service.TransferService;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {
    @Autowired
    private TransferService transferService;

    @PostMapping("/to-agent")
    public ResponseEntity<Void> transferToAgent(@RequestParam String sessionId) {
        transferService.transferToAgent(sessionId);
        return ResponseEntity.ok().build();
    }
}
