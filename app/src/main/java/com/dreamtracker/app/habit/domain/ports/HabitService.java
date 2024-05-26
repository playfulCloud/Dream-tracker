package com.dreamtracker.app.habit.domain.ports;

import com.dreamtracker.app.habit.domain.model.HabitTrack;
import com.dreamtracker.app.habit.adapters.api.HabitCategoryCreateRequest;
import com.dreamtracker.app.habit.adapters.api.HabitRequest;
import com.dreamtracker.app.habit.adapters.api.HabitResponse;
import com.dreamtracker.app.infrastructure.response.Page;
import java.util.List;
import java.util.UUID;

public interface HabitService {
    boolean delete(UUID id);
    List<HabitTrack>getHabitTrack(UUID id);
    HabitResponse createHabit(HabitRequest habitRequest);
    Page<HabitResponse>getAllUserHabits();
    HabitResponse updateHabit(UUID id,HabitRequest habitRequest);
    void linkCategoryWithHabit(UUID habitId, HabitCategoryCreateRequest categoryCreateRequest);
    HabitResponse getHabitById(UUID habitUUID);
}
