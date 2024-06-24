package com.dreamtracker.app.view.adapters.api;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
}
