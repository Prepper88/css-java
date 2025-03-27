package org.uwindsor.comp8117.cssjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8117.cssjava.dto.Customer;
import org.uwindsor.comp8117.cssjava.dto.Message;
import org.uwindsor.comp8117.cssjava.dto.Session;
import org.uwindsor.comp8117.cssjava.dto.SessionView;
import org.uwindsor.comp8117.cssjava.enums.SessionStatus;
import org.uwindsor.comp8117.cssjava.repository.CustomerRepository;
import org.uwindsor.comp8117.cssjava.repository.MessageRepository;
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

    @Autowired
    private MessageRepository messageRepository;

    public SessionView loadOrCreateSession(Long customerId) {
        Session session = sessionRepository.findByCustomerIdAndStatusIn(customerId, List.of(SessionStatus.ROBOT_PROCESSING.getValue(), SessionStatus.AGENT_PROCESSING.getValue())).orElse(null);
        if (session != null) {
            return loadSession(session.getSessionId());
        }

        session = new Session();
        session.setSessionId(UUID.randomUUID().toString());
        session.setCustomerId(customerId);
        session.setAgentId(0L);
        session.setCreatedAt(LocalDateTime.now());
        session.setStatus(SessionStatus.ROBOT_PROCESSING.getValue());
        Session newSession = sessionRepository.save(session);
        return loadSession(newSession.getSessionId());
    }

    public  SessionView loadSession(String sessionId) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));
        Customer customer = customerRepository.findById(session.getCustomerId()).orElseThrow(() -> new RuntimeException("Customer not found"));
        List<Message> messages = messageRepository.findBySessionId(sessionId);
        return buildSessionView(session, customer, messages);
    }

    public SessionView getActiveSessionByCustomerId(long customerId) {
        Session session = sessionRepository.findByCustomerIdAndStatusIn(customerId, List.of(SessionStatus.AGENT_PROCESSING.getValue(), SessionStatus.ROBOT_PROCESSING.getValue())).orElse(null);
        if (session == null) {
            return null;
        }
        Customer customer = customerRepository.findById(session.getCustomerId()).orElseThrow(() -> new RuntimeException("Customer not found"));
        List<Message> messages = messageRepository.findBySessionId(session.getSessionId());
        return buildSessionView(session, customer, messages);
    }

    public List<SessionView> getActiveSessionsByAgentId(long agentId) {
        List<Session> sessions = sessionRepository.findByAgentIdAndStatus(agentId, SessionStatus.AGENT_PROCESSING.getValue());
        return sessions.stream().map(session -> loadSession(session.getSessionId())).toList();
    }

    private SessionView buildSessionView(Session session, Customer customer, List<Message> messages) {
        SessionView sessionView = new SessionView();
        sessionView.setSessionId(session.getSessionId());
        sessionView.setCustomerId(session.getCustomerId());
        sessionView.setAgentId(session.getAgentId());
        sessionView.setCustomerName(customer.getUsername());
        sessionView.setStatus(session.getStatus());
        sessionView.setMessages(messages);
        return sessionView;

    }

    public void endSession(String sessionId, String endedBy, Long endedById) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));
        session.setStatus(SessionStatus.ENDED.getValue());
        session.setEndedBy(endedBy);
        session.setEndedById(endedById);
        sessionRepository.save(session);
    }

    public List<SessionView> loadSessionForAgent(Long agentId) {
        return sessionRepository.findByAgentIdAndStatus(agentId, SessionStatus.AGENT_PROCESSING.getValue()).stream().map(session -> loadSession(session.getSessionId())).toList();
    }
}
