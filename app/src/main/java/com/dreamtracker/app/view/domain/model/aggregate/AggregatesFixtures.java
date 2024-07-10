package com.dreamtracker.app.view.domain.model.aggregate;

import com.dreamtracker.app.view.adapters.api.*;
import com.dreamtracker.app.view.domain.ports.statistics.DomainQuantityOfHabitsService;

import java.util.UUID;

public interface AggregatesFixtures {

  default BreaksAggregate.BreaksAggregateBuilder getBreakAggregateBuilder(UUID habitUUID) {
    return BreaksAggregate.builder()
        .id(UUID.fromString("ccccb2ec-cf7a-4088-8109-d23d280e9379"))
        .isBreak(false)
        .breaksQuantity(0)
        .sumOfBreaks(0)
        .habitUUID(habitUUID);
    }

    default BreakComponentResponse.BreakComponentResponseBuilder getBreakStatsComponentResponse() {
       return BreakComponentResponse.builder()
               .averageBreak(0);
    }

    default DependingOnDayAggregate.DependingOnDayAggregateBuilder getDependingOnDayAggregateBuilder(UUID habitUUID) {
        return DependingOnDayAggregate.builder()
                .id(UUID.fromString("ccccb2ec-cf7a-4088-8109-d23d280e9379"))
                .habitUUID(habitUUID)
                .mondayDoneCount(0)
                .mondayUnDoneCount(0)
                .tuesdayDoneCount(0)
                .tuesdayUnDoneCount(0)
                .wednesdayDoneCount(0)
                .wednesdayUnDoneCount(0)
                .thursdayDoneCount(0)
                .thursdayUnDoneCount(0)
                .fridayDoneCount(0)
                .fridayUnDoneCount(0)
                .saturdayDoneCount(0)
                .saturdayUnDoneCount(0)
                .sundayDoneCount(0)
                .sundayUnDoneCount(0);
    }

    default DependingOnDayComponentResponse.DependingOnDayComponentResponseBuilder getDependingOnDayStatsComponentResponse(){
      return DependingOnDayComponentResponse.builder()
              .mondayRateSuccessRate(0)
              .tuesdayRateSuccessRate(0)
              .wednesdayRateSuccessRate(0)
              .thursdayRateSuccessRate(0)
              .fridayRateSuccessRate(0)
              .saturdayRateSuccessRate(0)
              .sundayRateSuccessRate(0);
    }

    default QuantityOfHabitsAggregate.QuantityOfHabitsAggregateBuilder getQuantityOfHabitsAggregateBuilder(UUID habitUUID){
        return QuantityOfHabitsAggregate.builder()
                .id(UUID.fromString("ccccb2ec-cf7a-4088-8109-d23d280e9379"))
                .habitUUID(habitUUID)
                .currentTrend(0)
                .unDoneHabits(0)
                .doneHabits(0);
    }



    default QuantityOfHabitsComponentResponse.QuantityOfHabitsComponentResponseBuilder getQuantityOfHabitsComponentResponse(){
       return QuantityOfHabitsComponentResponse.builder()
               .done(0)
               .undone(0)
               .trend(DomainQuantityOfHabitsService.TrendStatus.STAGNATION.toString());
    }

    default StreakAggregate.StreakAggregateBuilder getStreakAggregateBuilder(UUID habitUUID){
      return StreakAggregate.builder()
              .habitUUID(habitUUID)
              .id(UUID.fromString("ccccb2ec-cf7a-4088-8109-d23d280e9379"))
              .longestStreak(0)
              .currentStreak(0);
    }

    default StreakComponentResponse.StreakComponentResponseBuilder getStreakComponentResponseBuilder(){
      return StreakComponentResponse.builder()
              .actual(0)
              .longest(0);
    }


    default SingleDayAggregate.SingleDayAggregateBuilder getSingleDayAggregateBuilder(UUID habitUUID){
      return SingleDayAggregate.builder()
              .id(UUID.fromString("ccccb2ec-cf7a-4088-8109-d23d280e9379"))
              .habitUUID(habitUUID)
              .actualCount(0)
              .mostDone(0);
    }


    default SingleDayComponentResponse.SingleDayComponentResponseBuilder getSingleDayComponentResponseBuilder(String date){
      return SingleDayComponentResponse.builder()
              .actual(0)
              .most(0)
              .date(date);
    }
}
