package com.dreamtracker.app.view.adapters.api;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;


@RequiredArgsConstructor
@Builder
@Getter
public class BreakComponentResponse extends StatsComponentResponse {
    private final double averageBreak;


    @Override
    public String toString() {
        return "BreakComponentResponse{" +
                "averageBreak=" + averageBreak +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BreakComponentResponse that = (BreakComponentResponse) o;
        return Double.compare(averageBreak, that.averageBreak) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(averageBreak);
    }
}
