package com.dreamtracker.app.domain.request;

import lombok.Builder;

@Builder
public record HabitRequest(String name, String action, String frequency, String duration, String difficulty) {
}
