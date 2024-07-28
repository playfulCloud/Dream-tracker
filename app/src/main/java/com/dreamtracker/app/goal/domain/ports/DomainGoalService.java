package com.dreamtracker.app.goal.domain.ports;

import com.dreamtracker.app.goal.adapters.api.GoalRequest;
import com.dreamtracker.app.goal.adapters.api.GoalResponse;
import com.dreamtracker.app.goal.domain.model.Goal;
import com.dreamtracker.app.habit.domain.ports.HabitRepositoryPort;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.infrastructure.repository.SpringDataUserRepository;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import lombok.Data;

@Data
public class DomainGoalService implements GoalService {

  private final GoalRepositoryPort goalRepositoryPort;
  private final SpringDataUserRepository springDataUserRepository;
  private final CurrentUserProvider currentUserProvider;
  private final HabitRepositoryPort habitRepositoryPort;

  @Override
  @Transactional
  public GoalResponse createGoal(GoalRequest goalRequest) {

    var goalToCreate =
        Goal.builder()
            .name(goalRequest.name())
            .duration(goalRequest.duration())
            .habitUUID(goalRequest.habitID())
            .completionCount(goalRequest.completionCount())
            .userUUID(currentUserProvider.getCurrentUser())
            .build();

    var habitToBeAdded =
        habitRepositoryPort
            .findById(goalRequest.habitID())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    var goalSavedToDB = goalRepositoryPort.save(goalToCreate);
    habitToBeAdded.getGoals().add(goalSavedToDB);
    habitRepositoryPort.save(habitToBeAdded);
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
  public GoalResponse getGoalById(UUID id) {
    var goal =
        goalRepositoryPort
            .findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    return mapToResponse(goal);
  }

  @Override
  public GoalResponse increaseCompletionCount(UUID id) {
    var goal =
        goalRepositoryPort
            .findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    goal.increaseCompletionCount();
    var goalSavedToDb = goalRepositoryPort.save(goal);
    return mapToResponse(goalSavedToDb);
  }

  public GoalResponse mapToResponse(Goal goal) {
    return GoalResponse.builder()
        .id(goal.getUuid())
        .name(goal.getName())
        .duration(goal.getDuration())
        .completionCount(goal.getCompletionCount())
        .habitID(goal.getHabitUUID())
        .build();
  }
}
