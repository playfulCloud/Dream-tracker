package com.dreamtracker.app.habit.adapters.api;

import com.dreamtracker.app.habit.domain.model.Category;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record HabitResponse(UUID id, String name, String action, String duration, String difficulty, String status, List<Category> categories) {
}
