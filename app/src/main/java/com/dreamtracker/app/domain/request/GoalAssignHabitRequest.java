package com.dreamtracker.app.domain.request;

import java.util.UUID;

public record GoalAssignHabitRequest(UUID habitId, int completionCount) {}
