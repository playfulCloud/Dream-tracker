package com.dreamtracker.app.domain.request;

import lombok.Builder;

import java.util.UUID;


@Builder
public record HabitCategoryCreateRequest(UUID id) {
}
