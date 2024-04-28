package com.dreamtracker.service;

import com.dreamtracker.entity.Habit;
import com.dreamtracker.entity.HabitTrack;
import com.dreamtracker.request.HabitTrackingRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HabitService {
    Optional<Habit> save(Habit habit);
    Optional<Habit>findHabitById(UUID id);
    void deleteById(UUID id);
    Optional<HabitTrack>trackTheHabit(HabitTrackingRequest habitTrackingRequest);
    Optional<List<HabitTrack>>getHabitTrack(UUID id);
}
