package com.dreamtracker.app.infrastructure.fixtures;

import com.dreamtracker.app.domain.entity.HabitTrack;
import com.dreamtracker.app.domain.request.HabitTrackingRequest;
import com.dreamtracker.app.domain.response.HabitTrackResponse;
import com.dreamtracker.app.infrastructure.utils.HabitTrackStatus;

import java.util.UUID;

public interface HabitTrackFixture {
  default HabitTrack.HabitTrackBuilder getSampleHabitTrack(UUID habitUUID,String date) {
    return (HabitTrack.builder()
        .id(UUID.fromString("511cc580-1828-46f7-81f3-57d67fecff48"))
        .habitUUID(habitUUID)
        .date(date)
        .status(HabitTrackStatus.DONE.toString()));
  }

  default HabitTrackResponse.HabitTrackResponseBuilder getSampleHabitTrackResponse(String date) {
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
