package com.dreamtracker.app.service;

import com.dreamtracker.app.domain.request.HabitTrackingRequest;
import com.dreamtracker.app.domain.response.HabitTrackResponse;
import com.dreamtracker.app.domain.response.Page;

import java.util.UUID;

public interface HabitTrackService {
    HabitTrackResponse trackTheHabit(HabitTrackingRequest habitTrackingRequest);
    Page<HabitTrackResponse> getAllTracksOfHabit(UUID id);
}
