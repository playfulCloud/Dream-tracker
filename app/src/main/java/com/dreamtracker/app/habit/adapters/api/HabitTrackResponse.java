package com.dreamtracker.app.habit.adapters.api;

import lombok.Builder;

import java.time.Instant;


@Builder
public record HabitTrackResponse(Instant date, String status) {

}
