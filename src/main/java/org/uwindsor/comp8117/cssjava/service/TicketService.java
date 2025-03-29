package org.uwindsor.comp8117.cssjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8117.cssjava.dto.*;
import org.uwindsor.comp8117.cssjava.repository.CustomerRepository;
import org.uwindsor.comp8117.cssjava.repository.TicketRepository;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class TicketService {
    @Autowired
    private OrderService orderService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CustomerRepository customerRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy, h:mma", Locale.ENGLISH);

    public TicketPage getTicketPage(String sessionId) {
        SessionView sessionView = sessionService.loadSession(sessionId);

        if (sessionView != null) {
            TicketPage ticketPage = new TicketPage();

            Customer customer = customerRepository.findById(sessionView.getCustomerId()).orElseThrow(() -> new RuntimeException("Customer not found"));
            ticketPage.setCustomerInfoCard(buildCustomerInfoCard(customer));

            OrderCard orderCard = sessionView.getOrderCard();
            if (orderCard != null) {
                ticketPage.setOrderInfoCard(buildOrderInfoCard(orderCard));

                List<OrderField> orderFields = orderService.findOrderFieldsByOrderId(orderCard.getOrderId());
                ticketPage.setExtraInfoCard(buildExtraInfoCard("Installation Info", orderFields));
            }

            if (sessionView.getTicketId() != null) {
                Ticket ticket = ticketRepository.findById(sessionView.getTicketId());
                ticketPage.setTicket(ticket);
            }
            return ticketPage;
        }
        return null;
    }

    public InfoCard buildCustomerInfoCard(Customer customer) {
        InfoCard infoCard = new InfoCard("Customer Info");
        Field[] fields = Customer.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.getName().equals("password")) {
                    continue;
                }
                Object value = field.get(customer);
                infoCard.addField(field.getName(), value.toString());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return infoCard;
    }

    public InfoCard buildOrderInfoCard(OrderCard orderCard) {
        InfoCard infoCard = new InfoCard("Order Info");
        infoCard.addField("Order ID", orderCard.getOrderId());
        infoCard.addField("Order Status", orderCard.getPayStatus());
        infoCard.addField("Order Date", orderCard.getCreatedAt().format(formatter));
        infoCard.addField("Delivery Status", orderCard.getDeliveryStatus());
        infoCard.addField("Price", orderCard.getPrice());
        return infoCard;
    }

    public InfoCard buildExtraInfoCard(String cardName, List<OrderField> orderFields) {
        if (orderFields == null || orderFields.isEmpty()) {
            return null;
        }
        InfoCard infoCard = new InfoCard(cardName);
        for (OrderField orderField : orderFields) {
            infoCard.addField(orderField.getName(), orderField.getValue());
        }
        return infoCard;
    }

    public Ticket addOrUpdateTicket(Ticket ticket) {
        if (ticket.getId() == null) {
            ticket.setCreatedAt(LocalDateTime.now());
            Ticket savedTicket = ticketRepository.save(ticket);

            String sessionId = ticket.getSessionId();
            Session session = sessionService.loadSession(sessionId).toSession();
            session.setTicketId(savedTicket.getId());
            sessionService.updateSession(session);
        }
        return ticketRepository.save(ticket);
    }
}
