package com.dreamtracker.app.domain.request;

import lombok.Builder;

import java.util.UUID;
@Builder
public record HabitTrackingRequest(UUID habitId, String status) {
}
