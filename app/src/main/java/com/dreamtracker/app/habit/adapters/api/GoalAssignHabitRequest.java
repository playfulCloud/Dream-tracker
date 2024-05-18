package com.dreamtracker.app.habit.adapters.api;

import java.util.UUID;

public record GoalAssignHabitRequest(UUID habitId, int completionCount) {}
