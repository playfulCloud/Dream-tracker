package com.dreamtracker.app.view.adapters.api;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Builder
@Getter
public class StreakComponentResponse extends StatsComponentResponse{
    private final int longest;
    private final int actual;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StreakComponentResponse that)) return false;
        return longest == that.longest && actual == that.actual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(longest, actual);
    }
}
