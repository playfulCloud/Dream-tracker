package com.dreamtracker.app.habit.domain.ports;

import com.dreamtracker.app.habit.domain.model.HabitTrack;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HabitTrackRepositoryPort {
    Optional<HabitTrack>findById(UUID id);
    List<HabitTrack>findByHabitUUID(UUID habitUUID);
    HabitTrack save(HabitTrack habitTrack);
    List<HabitTrack> findAllByUserUUID(UUID userUUID);
}
