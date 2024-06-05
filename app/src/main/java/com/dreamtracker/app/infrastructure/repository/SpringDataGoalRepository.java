package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.goal.domain.model.Goal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataGoalRepository extends JpaRepository<Goal, UUID> {
    List<Goal> findByUserUUID(UUID id);
}
