package com.dreamtracker.app.view.domain.model.aggregate;

import com.dreamtracker.app.view.adapters.api.BreakComponentResponse;

import java.util.UUID;

public interface AggregatesFixtures {

  default BreaksAggregate.BreaksAggregateBuilder getBreakAggregateSavedBuilder(UUID habitUUID) {
    return BreaksAggregate.builder()
        .id(UUID.fromString("ccccb2ec-cf7a-4088-8109-d23d280e9379"))
        .isBreak(false)
        .breaksQuantity(0)
        .sumOfBreaks(0)
        .habitUUID(habitUUID);
    }

  default BreaksAggregate.BreaksAggregateBuilder getBreakAggregatePreSavedBuilder(UUID habitUUID) {
    return BreaksAggregate.builder()
        .isBreak(false)
        .breaksQuantity(0)
        .sumOfBreaks(0)
        .habitUUID(habitUUID);
    }

    default BreakComponentResponse.BreakComponentResponseBuilder getBreakStatsComponentResponse() {
       return BreakComponentResponse.builder()
               .averageBreak(0);
    }

    default DependingOnDayAggregate.DependingOnDayAggregateBuilder defaultDependingOnDayAggregate() {
        return DependingOnDayAggregate.builder()
                .id(UUID.randomUUID())
                .habitUUID(UUID.randomUUID())
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


}
