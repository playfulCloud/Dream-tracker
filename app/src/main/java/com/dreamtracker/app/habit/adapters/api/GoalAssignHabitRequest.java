package com.dreamtracker.app.habit.adapters.api;

import lombok.Builder;

import java.util.UUID;
@Builder
public record GoalAssignHabitRequest(UUID habitId, int completionCount) {}
