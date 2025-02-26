package org.uwindsor.comp8117.cssjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8117.cssjava.dto.Agent;
import org.uwindsor.comp8117.cssjava.dto.Customer;
import org.uwindsor.comp8117.cssjava.dto.Session;
import org.uwindsor.comp8117.cssjava.dto.SessionView;
import org.uwindsor.comp8117.cssjava.enums.AgentStatus;
import org.uwindsor.comp8117.cssjava.enums.SessionStatus;
import org.uwindsor.comp8117.cssjava.repository.AgentRepository;
import org.uwindsor.comp8117.cssjava.repository.CustomerRepository;
import org.uwindsor.comp8117.cssjava.repository.SessionRepository;

import java.util.List;

@Service
public class TransferService {
    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private NodePushService nodePushService;

    @Autowired
    private CustomerRepository customerRepository;

    public void transferToAgent(String sessionId) {
        List<Agent> availableAgents = agentRepository.findByStatus(AgentStatus.AVAILABLE.getValue());
        for (Agent agent : availableAgents) {
            List<Session> activeSessions = sessionRepository.findByAgentIdAndStatus(agent.getId(), SessionStatus.AGENT_PROCESSING.getValue());
            if (activeSessions.size() < agent.getMaxCustomers()) {
                Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));
                session.setAgentId(agent.getId());
                session.setStatus(SessionStatus.AGENT_PROCESSING.getValue());
                sessionRepository.save(session);

                if (activeSessions.size() + 1 >= agent.getMaxCustomers()) {
                    agent.setStatus(AgentStatus.BUSY.getValue());
                    agentRepository.save(agent);
                }

                SessionView sessionView = new SessionView();
                Customer customer = customerRepository.findById(session.getCustomerId()).orElseThrow(() -> new RuntimeException("Customer not found"));
                sessionView.setCustomerName(customer.getUsername());
                sessionView.setSessionId(sessionId);
                sessionView.setAgentId(agent.getId());
                sessionView.setCustomerId(customer.getId());
                sessionView.setStatus(session.getStatus());
                nodePushService.notifyNewSession(sessionView);
                return;
            }
        }
        throw new RuntimeException("No available agents");
    }
}
