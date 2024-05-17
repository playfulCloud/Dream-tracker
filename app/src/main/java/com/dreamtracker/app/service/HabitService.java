package com.dreamtracker.app.service;

import com.dreamtracker.app.domain.entity.HabitTrack;
import com.dreamtracker.app.domain.request.HabitCategoryCreateRequest;
import com.dreamtracker.app.domain.request.HabitRequest;
import com.dreamtracker.app.domain.response.HabitResponse;
import com.dreamtracker.app.domain.response.Page;
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
