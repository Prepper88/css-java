package org.uwindsor.comp8117.cssjava.dto;

import lombok.Data;

@Data
public class TicketPage {
    // Customer Information
    private InfoCard customerInfoCard;

    // Order Information
    private InfoCard orderInfoCard;

    // Order Extra Information
    private InfoCard extraInfoCard;

    // Ticket Information
    private Ticket ticket;
}
