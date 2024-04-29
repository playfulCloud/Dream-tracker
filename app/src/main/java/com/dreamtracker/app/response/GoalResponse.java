package com.dreamtracker.app.response;

import com.dreamtracker.app.entity.Habit;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record GoalResponse(UUID id, String name, String duration, List<Habit> habitList) {}
