package com.dreamtracker.app.fixtures;

import com.dreamtracker.app.entity.Habit;
import com.dreamtracker.app.request.HabitRequest;
import com.dreamtracker.app.response.HabitResponse;
import com.dreamtracker.app.utils.HabitDifficulty;
import com.dreamtracker.app.utils.HabitFrequency;
import com.dreamtracker.app.utils.HabitStatus;
import java.util.ArrayList;
import java.util.UUID;

public interface HabitFixture {

  default Habit.HabitBuilder getSampleHabitBuilder(UUID userUUID) {
    return Habit.builder()
        .id(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"))
        .name("exercising")
        .action("hitting a gym")
        .frequency(HabitFrequency.DAILY.toString())
        .duration("P30M")
        .difficulty(HabitDifficulty.EASY.toString())
        .status(HabitStatus.ACTIVE.toString())
        .categories(new ArrayList<>())
        .goals(new ArrayList<>())
        .userUUID(userUUID);
  }

  default Habit.HabitBuilder getSampleUpdatedHabitBuilder(UUID userUUID) {
    return Habit.builder()
            .id(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"))
            .name("reading")
            .action("30 pages per day")
            .frequency(HabitFrequency.DAILY.toString())
            .duration("P30M")
            .difficulty(HabitDifficulty.EASY.toString())
            .status(HabitStatus.ACTIVE.toString())
            .categories(new ArrayList<>())
            .goals(new ArrayList<>())
            .userUUID(userUUID);
  }

  default HabitResponse.HabitResponseBuilder getSampleHabitResponseBuilder(UUID userUUID){
      return HabitResponse.builder()
              .id(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"))
              .name("exercising")
              .action("hitting a gym")
              .duration("P30M")
              .difficulty(HabitDifficulty.EASY.toString())
              .status(HabitStatus.ACTIVE.toString());
  }



  default HabitRequest.HabitRequestBuilder getSampleHabitRequestBuilder(){
    return HabitRequest.builder()
            .name("exercising")
            .action("hitting a gym")
            .duration("P30M")
            .frequency(HabitFrequency.DAILY.toString())
            .difficulty(HabitDifficulty.EASY.toString());
  }

  default HabitRequest.HabitRequestBuilder getSampleHabitRequestUpdateBuilder(){
    return HabitRequest.builder()
            .name("reading")
            .action("30 pages per day")
            .duration("P30M")
            .frequency(HabitFrequency.DAILY.toString())
            .difficulty(HabitDifficulty.EASY.toString());
  }

  default HabitResponse.HabitResponseBuilder getSampleUpdatedHabitResponseBuilder(){
    return HabitResponse.builder()
            .id(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"))
            .name("reading")
            .action("30 pages per day")
            .duration("P30M")
            .difficulty(HabitDifficulty.EASY.toString())
            .status(HabitStatus.ACTIVE.toString());


  }
}
