package com.dreamtracker.app.service;

import com.dreamtracker.app.entity.Goal;
import com.dreamtracker.app.request.GoalAssignHabitRequest;
import com.dreamtracker.app.request.GoalRequest;
import com.dreamtracker.app.response.GoalResponse;
import com.dreamtracker.app.response.Page;
import java.util.Optional;
import java.util.UUID;

public interface GoalService {
    GoalResponse createGoal(GoalRequest goalRequest);
    boolean delete(UUID id);
    boolean existsById(UUID id);
    GoalResponse updateGoal(UUID id, GoalRequest goalRequest);
    Page<GoalResponse> getAllUserGoals();
    void AssociateHabitWithGoal(UUID goalId, GoalAssignHabitRequest goalAssignHabitRequest);
    GoalResponse getGoalById(UUID id);
}
