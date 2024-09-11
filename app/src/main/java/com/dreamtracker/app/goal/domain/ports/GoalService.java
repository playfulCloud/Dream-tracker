package com.dreamtracker.app.goal.domain.ports;

import com.dreamtracker.app.goal.adapters.api.GoalRequest;
import com.dreamtracker.app.goal.adapters.api.GoalResponse;
import com.dreamtracker.app.goal.domain.model.Goal;
import com.dreamtracker.app.habit.adapters.api.GoalAssignHabitRequest;
import com.dreamtracker.app.infrastructure.response.Page;
import java.util.UUID;

public interface GoalService {
    GoalResponse createGoal(GoalRequest goalRequest);
    boolean delete(UUID id);
    GoalResponse updateGoal(UUID id, GoalRequest goalRequest);
    Page<GoalResponse> getAllUserGoals();
    GoalResponse getGoalById(UUID id);
    GoalResponse increaseCompletionCount(UUID id);
    boolean markGoalAsFailedIfNotCompleted();
    Goal setStatusBasedOnDate(Goal goal);
}
