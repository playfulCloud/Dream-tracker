package com.dreamtracker.app.habit.adapters.api;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record HabitTrackResponse(OffsetDateTime date, String status) {

}
