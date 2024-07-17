package com.dreamtracker.app.fixtures;

import com.dreamtracker.app.habit.domain.model.HabitTrack;
import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.habit.adapters.api.HabitTrackResponse;
import com.dreamtracker.app.habit.domain.model.HabitTrackStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface HabitTrackFixture {
  default HabitTrack.HabitTrackBuilder getSampleHabitTrack(UUID habitUUID, OffsetDateTime date) {
    return (HabitTrack.builder()
        .id(UUID.fromString("511cc580-1828-46f7-81f3-57d67fecff48"))
        .habitUUID(habitUUID)
        .date(date)
        .status(HabitTrackStatus.DONE.toString()));
  }

  default HabitTrackResponse.HabitTrackResponseBuilder getSampleHabitTrackResponse(OffsetDateTime date) {
    return HabitTrackResponse.builder()
        .date(date)
        .status(HabitTrackStatus.DONE.toString());
  }

  default HabitTrackingRequest.HabitTrackingRequestBuilder getSampleHabitTrackRequest(UUID habitUUID){
      return HabitTrackingRequest.builder()
              .habitId(habitUUID)
              .status(HabitTrackStatus.DONE.toString());
  }


}
