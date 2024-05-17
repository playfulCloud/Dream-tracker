package com.dreamtracker.app.domain.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record HabitResponse(UUID id, String name, String action, String duration, String difficulty, String status) {
}
