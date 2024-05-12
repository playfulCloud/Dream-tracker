package com.dreamtracker.app.request;

import lombok.Builder;

@Builder
public record HabitRequest(String name, String action, String frequency, String duration, String difficulty) {
}
