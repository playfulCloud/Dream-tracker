package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.habit.domain.model.HabitTrack;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataHabitTrackRepository extends JpaRepository<HabitTrack, UUID> {
    List<HabitTrack> findByHabitUUID(UUID id);
}
