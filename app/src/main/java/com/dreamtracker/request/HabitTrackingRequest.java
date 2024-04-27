package com.dreamtracker.request;

import java.util.UUID;

public record HabitTrackingRequest(UUID habitId, String status) {
}
