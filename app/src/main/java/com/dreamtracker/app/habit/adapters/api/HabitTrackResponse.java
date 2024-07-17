package com.dreamtracker.app.habit.adapters.api;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Objects;

@Builder
public record HabitTrackResponse(OffsetDateTime date, String status) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HabitTrackResponse that = (HabitTrackResponse) o;
        return Objects.equals(status, that.status) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, status);
    }
}
