package com.dreamtracker.app.view.domain.model.aggregate;

import java.util.UUID;

public interface AggregatesFixtures {


    default BreaksAggregate.BreaksAggregateBuilder defaultBreaksAggregate(UUID habitUUID) {
        return BreaksAggregate.builder()
                .id(UUID.fromString("ccccb2ec-cf7a-4088-8109-d23d280e9379"))
                .habitUUID(habitUUID);
    }

    default BreaksAggregate.BreaksAggregateBuilder preSavedDefaultBreaksAggregate() {
        return BreaksAggregate.builder();
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
