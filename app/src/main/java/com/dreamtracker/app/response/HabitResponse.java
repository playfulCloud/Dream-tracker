package com.dreamtracker.app.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record HabitResponse(UUID id, String name, String action, String duration, String difficulty, String status) {
}
