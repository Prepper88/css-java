package org.uwindsor.comp8117.cssjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8117.cssjava.dto.Agent;
import org.uwindsor.comp8117.cssjava.dto.Customer;
import org.uwindsor.comp8117.cssjava.dto.Session;
import org.uwindsor.comp8117.cssjava.dto.SessionView;
import org.uwindsor.comp8117.cssjava.enums.SessionStatus;
import org.uwindsor.comp8117.cssjava.repository.AgentRepository;
import org.uwindsor.comp8117.cssjava.repository.CustomerRepository;
import org.uwindsor.comp8117.cssjava.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Session createSessionIfNotExist(Long customerId) {
        Session session = sessionRepository.findByCustomerIdAndStatusIn(customerId, List.of(SessionStatus.SYSTEM_PROCESSING.getValue(), SessionStatus.AGENT_PROCESSING.getValue())).orElse(null);
        if (session != null) {
            return session;
        }

        session = new Session();
        session.setSessionId(UUID.randomUUID().toString());
        session.setCustomerId(customerId);
        session.setAgentId(0L);
        session.setCreatedAt(LocalDateTime.now());
        session.setStatus(SessionStatus.SYSTEM_PROCESSING.getValue());
        return sessionRepository.save(session);
    }

    public SessionView getActiveSessionByCustomerId(long customerId) {
        Session session = sessionRepository.findByCustomerIdAndStatusIn(customerId, List.of(SessionStatus.AGENT_PROCESSING.getValue(), SessionStatus.SYSTEM_PROCESSING.getValue())).orElse(null);
        if (session == null) {
            return null;
        }
        Customer customer = customerRepository.findById(session.getCustomerId()).orElseThrow(() -> new RuntimeException("Customer not found"));
        return buildSessionView(session, customer);
    }

    public List<SessionView> getActiveSessionsByAgentId(long agentId) {
        List<Session> sessions = sessionRepository.findByAgentIdAndStatus(agentId, SessionStatus.AGENT_PROCESSING.getValue());
        return sessions.stream().map(session -> {
            Customer customer = customerRepository.findById(session.getCustomerId()).orElseThrow(() -> new RuntimeException("Customer not found"));
            return buildSessionView(session, customer);
        }).toList();
    }

    private SessionView buildSessionView(Session session, Customer customer) {
        SessionView sessionView = new SessionView();
        sessionView.setSessionId(session.getSessionId());
        sessionView.setCustomerId(session.getCustomerId());
        sessionView.setAgentId(session.getAgentId());
        sessionView.setCustomerName(customer.getUsername());
        sessionView.setStatus(session.getStatus());
        return sessionView;

    }

    public void endSession(String sessionId, String endedBy, Long endedById) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));
        session.setStatus(SessionStatus.ENDED.getValue());
        session.setEndedBy(endedBy);
        session.setEndedById(endedById);
        sessionRepository.save(session);
    }

}
