package com.dreamtracker.app.fixtures;

import com.dreamtracker.app.entity.Habit;
import com.dreamtracker.app.entity.User;
import com.dreamtracker.app.utils.HabitDifficulty;
import com.dreamtracker.app.utils.HabitFrequency;
import com.dreamtracker.app.utils.HabitStatus;

import java.util.ArrayList;
import java.util.UUID;

public interface HabitFixture {

  default Habit.HabitBuilder getSampleHabitBuilder(User user) {
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
        .habitTrackList(new ArrayList<>())
        .user(user);
    }
}
