package com.dreamtracker.app.habit.domain.ports;

import com.dreamtracker.app.habit.domain.model.Habit;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HabitRepositoryPort {
    Boolean existsById(UUID id);
    void deleteById(UUID id);
    List<Habit> findByUserUUID(UUID userUUID);
    Optional<Habit>findById(UUID id);
    Habit save(Habit habit);
    List<Habit>findAll();
    List<Habit>findByStatus(String status);
    List<Habit> findByCoolDownTillAfter(Instant currentDate);
}
