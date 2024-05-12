package com.dreamtracker.app.fixtures;

import com.dreamtracker.app.entity.HabitTrack;
import com.dreamtracker.app.request.HabitTrackingRequest;
import com.dreamtracker.app.response.HabitTrackResponse;
import com.dreamtracker.app.utils.HabitTrackStatus;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
