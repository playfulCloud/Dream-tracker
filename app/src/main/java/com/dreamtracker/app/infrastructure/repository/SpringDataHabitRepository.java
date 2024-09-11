package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.habit.domain.model.Habit;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataHabitRepository extends JpaRepository<Habit, UUID> {
    List<Habit> findByUserUUID(UUID id);
    List<Habit> findByStatus(String status);
    List<Habit> findByCoolDownTillAfter(Instant currentDate);
}
