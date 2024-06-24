package com.dreamtracker.app.view.adapters.api;

import lombok.Builder;

import java.util.UUID;


@Builder
public record ViewRequest(String name, UUID habitUUID) {
}
