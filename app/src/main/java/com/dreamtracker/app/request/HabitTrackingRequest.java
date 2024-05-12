package com.dreamtracker.app.request;

import lombok.Builder;

import java.util.UUID;
@Builder
public record HabitTrackingRequest(UUID habitId, String status) {
}
