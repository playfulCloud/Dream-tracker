package com.dreamtracker.app.view.adapters.api;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CombinedComponentResponse {

  private double averageBreak;
  private int longest;
  private int actual;

  private int done;
  private int undone;
  private String trend;

  private int most;
  private int actualSingleDay;
  private OffsetDateTime date;

  private double mondayRateSuccessRate;
  private double tuesdayRateSuccessRate;
  private double wednesdayRateSuccessRate;
  private double thursdayRateSuccessRate;
  private double fridayRateSuccessRate;
  private double saturdayRateSuccessRate;
  private double sundayRateSuccessRate;
}
