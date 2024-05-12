package com.dreamtracker.app.service;

import com.dreamtracker.app.entity.HabitTrack;
import com.dreamtracker.app.request.HabitCategoryCreateRequest;
import com.dreamtracker.app.request.HabitRequest;
import com.dreamtracker.app.response.HabitResponse;
import com.dreamtracker.app.response.Page;
import java.util.List;
import java.util.UUID;

public interface HabitService {
    boolean delete(UUID id);
    List<HabitTrack>getHabitTrack(UUID id);
    HabitResponse createHabit(HabitRequest habitRequest);
    Page<HabitResponse>getAllUserHabits();
    HabitResponse updateHabit(UUID id,HabitRequest habitRequest);
    void linkCategoryWithHabit(UUID habitId, HabitCategoryCreateRequest categoryCreateRequest);
}
