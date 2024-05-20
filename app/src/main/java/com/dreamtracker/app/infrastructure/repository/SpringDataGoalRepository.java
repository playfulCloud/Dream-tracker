package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.goal.domain.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface SpringDataGoalRepository extends JpaRepository<Goal, UUID> {
    List<Goal> findByUserUUID(UUID id);
}
