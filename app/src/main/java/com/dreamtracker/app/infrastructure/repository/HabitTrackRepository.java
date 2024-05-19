package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.habit.domain.model.HabitTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HabitTrackRepository extends JpaRepository<HabitTrack, UUID> {
    List<HabitTrack> findByHabitUUID(UUID id);
}
