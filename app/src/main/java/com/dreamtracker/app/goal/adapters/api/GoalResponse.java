package com.dreamtracker.app.goal.adapters.api;

import com.dreamtracker.app.habit.domain.model.Habit;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record GoalResponse(UUID id, String name, String duration, List<Habit> habitList) {}

