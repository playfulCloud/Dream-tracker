package com.dreamtracker.app.habit.adapters.api;

import lombok.Builder;

import java.util.UUID;

@Builder
public record HabitResponse(UUID id, String name, String action, String duration, String difficulty, String status) {
}
