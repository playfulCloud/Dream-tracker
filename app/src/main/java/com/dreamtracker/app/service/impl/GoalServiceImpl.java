package com.dreamtracker.app.service.impl;

import com.dreamtracker.app.entity.Goal;
import com.dreamtracker.app.exception.EntityNotFoundException;
import com.dreamtracker.app.exception.EntitySaveException;
import com.dreamtracker.app.exception.ExceptionMessages;
import com.dreamtracker.app.repository.GoalRepository;
import com.dreamtracker.app.request.GoalAssignHabitRequest;
import com.dreamtracker.app.request.GoalRequest;
import com.dreamtracker.app.response.GoalResponse;
import com.dreamtracker.app.response.Page;
import com.dreamtracker.app.security.CurrentUserProvider;
import com.dreamtracker.app.service.GoalService;
import com.dreamtracker.app.service.HabitService;
import com.dreamtracker.app.service.UserService;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class GoalServiceImpl implements GoalService {

  private final GoalRepository goalRepository;
  private final UserService userService;
  private final CurrentUserProvider currentUserProvider;
  private final HabitService habitService;

  @Override
  public Optional<Goal> findById(UUID id) {
    return goalRepository.findById(id);
  }

  @Override
  public Optional<Goal> save(Goal goal) {
    return Optional.of(goalRepository.save(goal));
  }

  @Override
  public GoalResponse createGoal(GoalRequest goalRequest) {
    var ownerOfGoal =
        userService
            .findById(currentUserProvider.getCurrentUser())
            .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    var goalToCreate =
        Goal.builder()
            .name(goalRequest.name())
            .duration(goalRequest.duration())
            .habitList(new ArrayList<>())
            .user(ownerOfGoal)
            .build();

    var goalSavedToDB =
        save(goalToCreate).orElseThrow(() -> new EntitySaveException(ExceptionMessages.entitySaveExceptionMessage));
    ownerOfGoal.getGoals().add(goalSavedToDB);

    userService.save(ownerOfGoal);

    return mapToResponse(goalSavedToDB);
  }

  @Override
  public void deleteById(UUID id) {
    goalRepository.deleteById(id);
  }

  @Override
  public boolean delete(UUID id) {
    if (existsById(id)) {
      deleteById(id);
      return true;
    }
    return false;
  }

  @Override
  public boolean existsById(UUID id) {
    return goalRepository.existsById(id);
  }

  @Override
  public GoalResponse updateGoal(UUID id, GoalRequest goalRequest) {
    var goalToUpdate = findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    Optional.ofNullable(goalRequest.name()).ifPresent(goalToUpdate::setName);
    Optional.ofNullable(goalRequest.duration()).ifPresent(goalToUpdate::setDuration);

    var updatedGoal = save(goalToUpdate).orElseThrow(() -> new EntitySaveException(ExceptionMessages.entitySaveExceptionMessage));

    return mapToResponse(updatedGoal);
  }

  @Override
  public Page<GoalResponse> getAllUserGoals() {
    var ownerOfGoals =
        userService
            .findById(currentUserProvider.getCurrentUser())
            .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    var goals = ownerOfGoals.getGoals();
    var listOfGoalResponses = goals.stream().map(this::mapToResponse).toList();
    Page<GoalResponse> goalResponsePage = new Page<>();
    goalResponsePage.setItems(listOfGoalResponses);
    return goalResponsePage;
  }

  @Override
  public void AssociateHabitWithGoal(UUID goalId, GoalAssignHabitRequest goalAssignHabitRequest) {
    var goalToAddHabit =
        findById(goalId).orElseThrow(() -> new EntitySaveException(ExceptionMessages.entitySaveExceptionMessage));
    var habitToBeAdded = habitService.findHabitById(goalAssignHabitRequest.habitId()).orElseThrow(() ->new EntitySaveException(ExceptionMessages.entityNotFoundExceptionMessage));

    goalToAddHabit.getHabitList().add(habitToBeAdded);
    habitToBeAdded.getGoals().add(goalToAddHabit);

    save(goalToAddHabit).orElseThrow(() -> new EntitySaveException(ExceptionMessages.entitySaveExceptionMessage));
    habitService.save(habitToBeAdded).orElseThrow(() -> new EntitySaveException(ExceptionMessages.entitySaveExceptionMessage));
  }

  @Override
  public GoalResponse getGoalById(UUID id) {
    var goal = findById(id).orElseThrow(() -> new EntitySaveException(ExceptionMessages.entityNotFoundExceptionMessage));
    return mapToResponse(goal);
  }

  public GoalResponse mapToResponse(Goal goal) {
    return GoalResponse.builder()
        .id(goal.getUuid())
        .duration(goal.getDuration())
        .habitList(goal.getHabitList())
        .build();
  }
}
