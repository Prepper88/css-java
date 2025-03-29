package org.uwindsor.comp8117.cssjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uwindsor.comp8117.cssjava.dto.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, String> {
    Ticket findById(long id);
}
