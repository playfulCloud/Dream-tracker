package com.dreamtracker.app.view.adapters.api;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@Builder
@Getter
public class SingleDayComponentResponse extends StatsComponentResponse{
    private final int most;
    private final int actual;
    private final Instant date;


    @Override
    public String toString() {
        return "SingleDayComponentResponse{" +
                "most=" + most +
                ", actual=" + actual +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleDayComponentResponse that = (SingleDayComponentResponse) o;
        return most == that.most && actual == that.actual && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(most, actual, date);
    }

    @Override
    public void combineResponse(CombinedComponentResponse response){
        response.setMost(this.getMost());
        response.setActualSingleDay(this.actual);
        response.setDate(this.getDate());
    }
}
