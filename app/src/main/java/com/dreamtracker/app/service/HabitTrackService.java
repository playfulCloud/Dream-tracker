package com.dreamtracker.app.service;

import com.dreamtracker.app.entity.Habit;
import com.dreamtracker.app.entity.HabitTrack;
import com.dreamtracker.app.request.HabitTrackingRequest;
import com.dreamtracker.app.response.HabitTrackResponse;
import com.dreamtracker.app.response.Page;

import java.util.Optional;
import java.util.UUID;

public interface HabitTrackService {
    Optional<HabitTrack> save(HabitTrack habitTrack);
    Optional<Habit>findHabitTrackById(UUID id);
    HabitTrackResponse trackTheHabit(HabitTrackingRequest habitTrackingRequest);
    void deleteById(UUID id);
    Page<HabitTrackResponse> getAllTracksOfHabit(UUID id);
}
