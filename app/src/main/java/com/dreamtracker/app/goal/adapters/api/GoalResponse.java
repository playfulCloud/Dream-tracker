package com.dreamtracker.app.goal.adapters.api;

import com.dreamtracker.app.habit.domain.model.Habit;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record GoalResponse(UUID id, String name, String duration, UUID habitID, int completionCount,int currentCount,String status,String createdAt) {}

