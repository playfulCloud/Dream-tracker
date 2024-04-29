package com.dreamtracker.app.service;

import com.dreamtracker.app.entity.Habit;
import com.dreamtracker.app.entity.HabitTrack;
import com.dreamtracker.app.request.HabitRequest;
import com.dreamtracker.app.request.HabitTrackingRequest;
import com.dreamtracker.app.response.HabitResponse;
import com.dreamtracker.app.response.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HabitService {
    Optional<Habit> save(Habit habit);
    Optional<Habit>findHabitById(UUID id);
    void deleteById(UUID id);
    boolean delete(UUID id);
    boolean existsById(UUID id);
    Optional<HabitTrack>trackTheHabit(HabitTrackingRequest habitTrackingRequest);
    Optional<List<HabitTrack>>getHabitTrack(UUID id);
    HabitResponse createHabit(HabitRequest habitRequest);
    Page<HabitResponse>getAllUserHabits();
    HabitResponse updateHabit(UUID id,HabitRequest habitRequest);
}