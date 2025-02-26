package org.uwindsor.comp8117.cssjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uwindsor.comp8117.cssjava.dto.Session;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, String> {
    List<Session> findByAgentIdAndStatus(Long agentId, String status);
    Optional<Session> findByCustomerIdAndStatusIn(Long customerId, List<String> status);
    Optional<Session> findByCustomerIdAndStatus(Long customerId, String status);
}