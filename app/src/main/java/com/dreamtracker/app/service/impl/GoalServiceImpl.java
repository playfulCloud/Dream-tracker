package com.dreamtracker.app.service.impl;

import com.dreamtracker.app.domain.entity.Goal;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.domain.repository.GoalRepository;
import com.dreamtracker.app.domain.repository.HabitRepository;
import com.dreamtracker.app.domain.request.GoalAssignHabitRequest;
import com.dreamtracker.app.domain.request.GoalRequest;
import com.dreamtracker.app.domain.response.GoalResponse;
import com.dreamtracker.app.domain.response.Page;
import com.dreamtracker.app.infrastructure.security.CurrentUserProvider;
import com.dreamtracker.app.service.GoalService;
import com.dreamtracker.app.service.UserService;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class GoalServiceImpl implements GoalService {

  private final GoalRepository goalRepository;
  private final UserService userService;
  private final CurrentUserProvider currentUserProvider;
  private final HabitRepository habitRepository;

  @Override
  public GoalResponse createGoal(GoalRequest goalRequest) {
    var ownerOfGoal =
        userService
            .findById(currentUserProvider.getCurrentUser())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    var goalToCreate =
        Goal.builder()
            .name(goalRequest.name())
            .duration(goalRequest.duration())
            .habitList(new ArrayList<>())
            .userUUID(ownerOfGoal.getUuid())
            .build();

    var goalSavedToDB = goalRepository.save(goalToCreate);
    userService.save(ownerOfGoal);
    return mapToResponse(goalSavedToDB);
  }

  @Override
  public boolean delete(UUID id) {
    if (existsById(id)) {
      goalRepository.deleteById(id);
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
    var goalToUpdate =
        goalRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    Optional.ofNullable(goalRequest.name()).ifPresent(goalToUpdate::setName);
    Optional.ofNullable(goalRequest.duration()).ifPresent(goalToUpdate::setDuration);

    var updatedGoal = goalRepository.save(goalToUpdate);

    return mapToResponse(updatedGoal);
  }

  @Override
  public Page<GoalResponse> getAllUserGoals() {
    var listOfGoals = goalRepository.findByUserUUID(currentUserProvider.getCurrentUser());
    var listOfGoalResponses = listOfGoals.stream().map(this::mapToResponse).toList();
    var goalResponsePage = new Page<GoalResponse>(listOfGoalResponses);
    return goalResponsePage;
  }

  @Override
  @Transactional
  public void associateHabitWithGoal(UUID goalId, GoalAssignHabitRequest goalAssignHabitRequest) {
    var goalToAddHabit =
        goalRepository
            .findById(goalId)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    var habitToBeAdded =
        habitRepository
            .findById(goalAssignHabitRequest.habitId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    goalToAddHabit.getHabitList().add(habitToBeAdded);
    habitToBeAdded.getGoals().add(goalToAddHabit);

    goalRepository.save(goalToAddHabit);
    habitRepository.save(habitToBeAdded);
  }

  @Override
  public GoalResponse getGoalById(UUID id) {
    var goal =
        goalRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    return mapToResponse(goal);
  }

  public GoalResponse mapToResponse(Goal goal) {
    return GoalResponse.builder()
        .id(goal.getUuid())
        .name(goal.getName())
        .duration(goal.getDuration())
        .build();
  }
}
