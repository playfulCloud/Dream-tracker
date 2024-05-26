package com.dreamtracker.app.habit.adapters.api;

import lombok.Builder;

import java.util.UUID;
@Builder
public record HabitTrackingRequest(UUID habitId, String status) {
}
