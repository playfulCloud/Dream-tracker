package com.dreamtracker.app.goal.adapters.api;


import lombok.Builder;

import java.util.UUID;

@Builder
public record GoalRequest(String name, String duration, int completionCount, UUID habitID) {}
