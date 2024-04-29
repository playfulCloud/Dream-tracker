package com.dreamtracker.app.request;

import java.util.UUID;

public record GoalAssignHabitRequest(UUID habitId, int completionCount) {}
