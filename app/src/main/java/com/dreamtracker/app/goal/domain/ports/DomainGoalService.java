package com.dreamtracker.app.goal.domain.ports;

import com.dreamtracker.app.goal.domain.model.Goal;
import com.dreamtracker.app.habit.domain.ports.HabitRepositoryPort;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.habit.adapters.api.GoalAssignHabitRequest;
import com.dreamtracker.app.goal.adapters.api.GoalRequest;
import com.dreamtracker.app.goal.adapters.api.GoalResponse;
import com.dreamtracker.app.infrastructure.repository.SpringDataUserRepository;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.user.config.CurrentUserProvider;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import jakarta.transaction.Transactional;
import lombok.Data;

@Data
public class DomainGoalService implements GoalService {

  private final GoalRepositoryPort goalRepositoryPort;
  private final SpringDataUserRepository springDataUserRepository;
  private final CurrentUserProvider currentUserProvider;
  private final HabitRepositoryPort habitRepositoryPort;

  @Override
  public GoalResponse createGoal(GoalRequest goalRequest) {
    var ownerOfGoal =
        springDataUserRepository
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

    var goalSavedToDB = goalRepositoryPort.save(goalToCreate);
    springDataUserRepository.save(ownerOfGoal);
    return mapToResponse(goalSavedToDB);
  }

  @Override
  public boolean delete(UUID id) {
    if (goalRepositoryPort.existsById(id)) {
      goalRepositoryPort.deleteById(id);
      return true;
    }
    return false;
  }



  @Override
  public GoalResponse updateGoal(UUID id, GoalRequest goalRequest) {
    var goalToUpdate =
        goalRepositoryPort
            .findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    Optional.ofNullable(goalRequest.name()).ifPresent(goalToUpdate::setName);
    Optional.ofNullable(goalRequest.duration()).ifPresent(goalToUpdate::setDuration);

    var updatedGoal = goalRepositoryPort.save(goalToUpdate);

    return mapToResponse(updatedGoal);
  }

  @Override
  public Page<GoalResponse> getAllUserGoals() {
    var listOfGoals = goalRepositoryPort.findByUserUUID(currentUserProvider.getCurrentUser());
    var listOfGoalResponses = listOfGoals.stream().map(this::mapToResponse).toList();
    var goalResponsePage = new Page<GoalResponse>(listOfGoalResponses);
    return goalResponsePage;
  }

  @Override
  @Transactional
  public void associateHabitWithGoal(UUID goalId, GoalAssignHabitRequest goalAssignHabitRequest) {
    var goalToAddHabit =
        goalRepositoryPort
            .findById(goalId)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    var habitToBeAdded =
        habitRepositoryPort
            .findById(goalAssignHabitRequest.habitId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    goalToAddHabit.getHabitList().add(habitToBeAdded);
    habitToBeAdded.getGoals().add(goalToAddHabit);

    goalRepositoryPort.save(goalToAddHabit);
    habitRepositoryPort.save(habitToBeAdded);
  }

  @Override
  public GoalResponse getGoalById(UUID id) {
    var goal =
        goalRepositoryPort
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
