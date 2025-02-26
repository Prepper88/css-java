package org.uwindsor.comp8117.cssjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uwindsor.comp8117.cssjava.repository.AgentRepository;
import org.uwindsor.comp8117.cssjava.dto.Agent;

@Service
public class AgentService {
    @Autowired
    private AgentRepository agentRepository;

    public Agent login(String username, String password) {
        return agentRepository.findByUsernameAndPassword(username, password).orElse(null);
    }

    public void updateStatus(Long agentId, String status) {
        Agent agent = agentRepository.findById(agentId).orElseThrow(() -> new RuntimeException("Agent not found"));
        agent.setStatus(status);
        agentRepository.save(agent);
    }

    public void updateAgentInfo(Long agentId, String password, String status, int maxCustomers) {
        Agent agent = agentRepository.findById(agentId).orElseThrow(() -> new RuntimeException("Agent not found"));
        if (password != null) agent.setPassword(password);
        if (status != null) agent.setStatus(status);
        if (maxCustomers > 0) agent.setMaxCustomers(maxCustomers);
        agentRepository.save(agent);
    }
}
