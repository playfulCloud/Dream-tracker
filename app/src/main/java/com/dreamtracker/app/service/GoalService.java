package com.dreamtracker.app.service;

import com.dreamtracker.app.domain.request.GoalAssignHabitRequest;
import com.dreamtracker.app.domain.request.GoalRequest;
import com.dreamtracker.app.domain.response.GoalResponse;
import com.dreamtracker.app.domain.response.Page;

import java.util.UUID;

public interface GoalService {
    GoalResponse createGoal(GoalRequest goalRequest);
    boolean delete(UUID id);
    boolean existsById(UUID id);
    GoalResponse updateGoal(UUID id, GoalRequest goalRequest);
    Page<GoalResponse> getAllUserGoals();
    void associateHabitWithGoal(UUID goalId, GoalAssignHabitRequest goalAssignHabitRequest);
    GoalResponse getGoalById(UUID id);
}
