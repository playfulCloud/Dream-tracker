package com.dreamtracker.app.request;


import lombok.Builder;

@Builder
public record GoalRequest(String name, String duration) {}
