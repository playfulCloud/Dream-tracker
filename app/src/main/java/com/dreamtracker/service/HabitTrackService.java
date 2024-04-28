package com.dreamtracker.service;

import com.dreamtracker.entity.Habit;
import com.dreamtracker.entity.HabitTrack;

import java.util.Optional;
import java.util.UUID;

public interface HabitTrackService {
    Optional<HabitTrack> save(HabitTrack habitTrack);
    Optional<Habit>findHabitTrackById(UUID id);
    void deleteById(UUID id);
}
