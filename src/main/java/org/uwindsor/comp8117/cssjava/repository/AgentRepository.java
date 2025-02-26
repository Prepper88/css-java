package org.uwindsor.comp8117.cssjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uwindsor.comp8117.cssjava.dto.Agent;

import java.util.List;
import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent> findByUsernameAndPassword(String username, String password);
    List<Agent> findByStatus(String status);
}