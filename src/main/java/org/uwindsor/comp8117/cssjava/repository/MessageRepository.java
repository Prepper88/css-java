package org.uwindsor.comp8117.cssjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uwindsor.comp8117.cssjava.dto.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySessionId(String sessionId);
}
