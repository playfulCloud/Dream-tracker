package com.dreamtracker.app.habit.domain.ports;

import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.habit.adapters.api.HabitTrackResponse;
import com.dreamtracker.app.habit.domain.model.ChartResponse;
import com.dreamtracker.app.infrastructure.response.Page;

import java.util.UUID;

public interface HabitTrackService {
    HabitTrackResponse trackTheHabit(HabitTrackingRequest habitTrackingRequest);
    Page<HabitTrackResponse> getAllTracksOfHabit(UUID id);
    Page<ChartResponse> getChartsFromHabitTracks();
}
