package com.dreamtracker.app.view.adapters.api;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Builder
@Getter
public class QuantityOfHabitsComponentResponse extends StatsComponentResponse {
    private final int done;
    private final int undone;
    private final String trend;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuantityOfHabitsComponentResponse that)) return false;
        return done == that.done && undone == that.undone && Objects.equals(trend, that.trend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(done, undone, trend);
    }
}
