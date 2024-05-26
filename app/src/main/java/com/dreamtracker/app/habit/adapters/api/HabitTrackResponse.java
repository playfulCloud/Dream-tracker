package com.dreamtracker.app.habit.adapters.api;

import lombok.Builder;

@Builder
public record HabitTrackResponse(String date, String status) {}
