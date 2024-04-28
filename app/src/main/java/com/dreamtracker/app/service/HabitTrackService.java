package com.dreamtracker.app.service;

import com.dreamtracker.app.entity.Habit;
import com.dreamtracker.app.entity.HabitTrack;

import java.util.Optional;
import java.util.UUID;

public interface HabitTrackService {
    Optional<HabitTrack> save(HabitTrack habitTrack);
    Optional<Habit>findHabitTrackById(UUID id);
    void deleteById(UUID id);
}
