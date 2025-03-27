package org.uwindsor.comp8117.cssjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8117.cssjava.dto.Agent;
import org.uwindsor.comp8117.cssjava.dto.Message;
import org.uwindsor.comp8117.cssjava.dto.Session;
import org.uwindsor.comp8117.cssjava.dto.SessionView;
import org.uwindsor.comp8117.cssjava.enums.AgentStatus;
import org.uwindsor.comp8117.cssjava.enums.MessageType;
import org.uwindsor.comp8117.cssjava.enums.SessionStatus;
import org.uwindsor.comp8117.cssjava.enums.UserType;
import org.uwindsor.comp8117.cssjava.repository.AgentRepository;
import org.uwindsor.comp8117.cssjava.repository.SessionRepository;

import java.time.LocalDateTime;
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
    @Lazy
    private MessageService messageService;

    @Autowired
    private SessionService sessionService;

    private final String TRANSFER_SYSTEM_MESSAGE = "You are now connected to an agent";
    private final String TRANSFER_AGENT_MESSAGE = "Hello! This is Agent 00%d. I’ll be assisting you today. How can I help?";

    public void transferToAgent(String sessionId) {
        List<Agent> availableAgents = agentRepository.findByStatus(AgentStatus.AVAILABLE.getValue());

        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));

        if (availableAgents.isEmpty()) {
            messageService.pushSystemMessage(session, "No available agents, please try again later.");
            return;
        }

        for (Agent agent : availableAgents) {
            List<Session> activeSessions = sessionRepository.findByAgentIdAndStatus(agent.getId(), SessionStatus.AGENT_PROCESSING.getValue());
            if (activeSessions.size() < agent.getMaxCustomers()) {
                session.setAgentId(agent.getId());
                session.setStatus(SessionStatus.AGENT_PROCESSING.getValue());
                sessionRepository.save(session);

                if (activeSessions.size() + 1 >= agent.getMaxCustomers()) {
                    agent.setStatus(AgentStatus.BUSY.getValue());
                    agentRepository.save(agent);
                }
                SessionView sessionView = sessionService.loadSession(sessionId);
                nodePushService.notifyNewSession(sessionView);

                // Send transfer message
                messageService.pushSystemMessage(session, TRANSFER_SYSTEM_MESSAGE);
                Message message = Message.builder()
                        .sessionId(sessionId)
                        .senderType(UserType.AGENT.getValue())
                        .senderId(agent.getId())
                        .messageType(MessageType.TEXT.getValue())
                        .content(String.format(TRANSFER_AGENT_MESSAGE, agent.getId()))
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                messageService.pushMessageToCustomerAndAgent(session.getCustomerId(), agent.getId(), message);
                return;
            }
        }
    }
}
