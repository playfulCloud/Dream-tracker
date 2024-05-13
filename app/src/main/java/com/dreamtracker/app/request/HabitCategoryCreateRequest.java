package com.dreamtracker.app.request;

import lombok.Builder;

import java.util.UUID;


@Builder
public record HabitCategoryCreateRequest(UUID id) {
}
