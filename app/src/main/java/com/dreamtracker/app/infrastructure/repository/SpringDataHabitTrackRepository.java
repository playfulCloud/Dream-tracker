package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.habit.domain.model.HabitTrack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataHabitTrackRepository extends JpaRepository<HabitTrack, UUID> {
    List<HabitTrack> findByHabitUUID(UUID id);
}
