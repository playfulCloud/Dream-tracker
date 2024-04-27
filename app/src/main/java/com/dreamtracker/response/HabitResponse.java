package com.dreamtracker.response;

import java.util.UUID;

public record HabitResponse(UUID id, String name, String action, String duration, String difficulty, String status) {
}
