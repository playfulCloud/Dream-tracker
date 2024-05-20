package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.habit.domain.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface SpringDataHabitRepository extends JpaRepository<Habit, UUID> {
    List<Habit> findByUserUUID(UUID id);
}
