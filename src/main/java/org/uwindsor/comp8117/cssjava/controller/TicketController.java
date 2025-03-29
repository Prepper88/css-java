package org.uwindsor.comp8117.cssjava.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.uwindsor.comp8117.cssjava.dto.Ticket;
import org.uwindsor.comp8117.cssjava.dto.TicketPage;
import org.uwindsor.comp8117.cssjava.service.TicketService;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @GetMapping("/page")
    public TicketPage getTicketPage(@RequestParam String sessionId) {
        return ticketService.getTicketPage(sessionId);
    }

    @PostMapping("/add-or-update")
    public Ticket addOrUpdateTicket(@RequestBody Ticket ticket) {
        return ticketService.addOrUpdateTicket(ticket);
    }

    @PostMapping("/get-service-progress")
    public String getServiceProgress(@RequestParam String sessionId) {
        return ticketService.getServiceProgress(sessionId);
    }

}
