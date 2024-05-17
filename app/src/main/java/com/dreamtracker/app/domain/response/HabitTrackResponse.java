package com.dreamtracker.app.domain.response;

import lombok.Builder;

@Builder
public record HabitTrackResponse(String date, String status) {}
