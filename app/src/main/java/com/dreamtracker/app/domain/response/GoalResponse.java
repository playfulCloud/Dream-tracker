package com.dreamtracker.app.domain.response;

import com.dreamtracker.app.domain.habit.domain.Habit;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record GoalResponse(UUID id, String name, String duration, List<Habit> habitList) {}

