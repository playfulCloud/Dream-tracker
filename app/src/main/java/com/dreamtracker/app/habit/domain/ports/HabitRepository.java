package com.dreamtracker.app.habit.domain.ports;

import com.dreamtracker.app.habit.domain.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HabitRepository extends JpaRepository<Habit, UUID> {
    List<Habit> findByUserUUID(UUID id);
}
