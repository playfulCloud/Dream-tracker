package com.dreamtracker.app.domain.request;


import lombok.Builder;

@Builder
public record GoalRequest(String name, String duration) {}
