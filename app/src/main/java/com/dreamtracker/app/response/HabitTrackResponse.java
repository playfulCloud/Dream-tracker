package com.dreamtracker.app.response;

import lombok.Builder;

@Builder
public record HabitTrackResponse(String date, String status) {}
