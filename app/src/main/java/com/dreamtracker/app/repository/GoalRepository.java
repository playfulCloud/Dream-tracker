package com.dreamtracker.app.repository;

import com.dreamtracker.app.entity.Category;
import com.dreamtracker.app.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GoalRepository extends JpaRepository<Goal, UUID> {
    List<Goal> findByUserUUID(UUID id);
}
