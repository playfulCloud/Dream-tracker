package com.dreamtracker.app.goal.adapters.api;


import lombok.Builder;

@Builder
public record GoalRequest(String name, String duration) {}
