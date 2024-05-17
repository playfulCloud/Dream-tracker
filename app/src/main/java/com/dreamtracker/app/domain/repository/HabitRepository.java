package com.dreamtracker.app.domain.repository;

import com.dreamtracker.app.domain.habit.domain.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HabitRepository extends JpaRepository<Habit, UUID> {
    List<Habit> findByUserUUID(UUID id);
}
