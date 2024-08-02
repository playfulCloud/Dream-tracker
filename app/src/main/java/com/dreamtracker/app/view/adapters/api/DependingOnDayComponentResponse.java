package com.dreamtracker.app.view.adapters.api;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Builder
@Getter
public class DependingOnDayComponentResponse extends StatsComponentResponse{
    private final double mondayRateSuccessRate;
    private final double tuesdayRateSuccessRate;
    private final double wednesdayRateSuccessRate;
    private final double thursdayRateSuccessRate;
    private final double fridayRateSuccessRate;
    private final double saturdayRateSuccessRate;
    private final double sundayRateSuccessRate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DependingOnDayComponentResponse that)) return false;
        return Double.compare(mondayRateSuccessRate, that.mondayRateSuccessRate) == 0 && Double.compare(tuesdayRateSuccessRate, that.tuesdayRateSuccessRate) == 0 && Double.compare(wednesdayRateSuccessRate, that.wednesdayRateSuccessRate) == 0 && Double.compare(thursdayRateSuccessRate, that.thursdayRateSuccessRate) == 0 && Double.compare(fridayRateSuccessRate, that.fridayRateSuccessRate) == 0 && Double.compare(saturdayRateSuccessRate, that.saturdayRateSuccessRate) == 0 && Double.compare(sundayRateSuccessRate, that.sundayRateSuccessRate) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mondayRateSuccessRate, tuesdayRateSuccessRate, wednesdayRateSuccessRate, thursdayRateSuccessRate, fridayRateSuccessRate, saturdayRateSuccessRate, sundayRateSuccessRate);
    }

    @Override
    public String toString() {
        return "DependingOnDayComponentResponse{" +
                "mondayRateSuccessRate=" + mondayRateSuccessRate +
                ", tuesdayRateSuccessRate=" + tuesdayRateSuccessRate +
                ", wednesdayRateSuccessRate=" + wednesdayRateSuccessRate +
                ", thursdayRateSuccessRate=" + thursdayRateSuccessRate +
                ", fridayRateSuccessRate=" + fridayRateSuccessRate +
                ", saturdayRateSuccessRate=" + saturdayRateSuccessRate +
                ", sundayRateSuccessRate=" + sundayRateSuccessRate +
                '}';
    }

    @Override
    public void combineResponse(CombinedComponentResponse response){
        response.setMondayRateSuccessRate(this.getMondayRateSuccessRate());
        response.setTuesdayRateSuccessRate(this.getTuesdayRateSuccessRate());
        response.setWednesdayRateSuccessRate(this.getWednesdayRateSuccessRate());
        response.setThursdayRateSuccessRate(this.getThursdayRateSuccessRate());
        response.setFridayRateSuccessRate(this.getFridayRateSuccessRate());
        response.setSaturdayRateSuccessRate(this.getSaturdayRateSuccessRate());
        response.setSundayRateSuccessRate(this.getSundayRateSuccessRate());
    }
}
