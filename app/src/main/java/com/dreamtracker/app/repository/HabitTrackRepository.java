package com.dreamtracker.app.repository;

import com.dreamtracker.app.entity.HabitTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HabitTrackRepository extends JpaRepository<HabitTrack, UUID> {
    List<HabitTrack> findByHabitUUID(UUID id);
}
