package com.dreamtracker.app.habit.domain.ports;

import com.dreamtracker.app.goal.domain.ports.DomainGoalService;
import com.dreamtracker.app.goal.domain.ports.GoalService;
import com.dreamtracker.app.habit.adapters.api.HabitCategoryCreateRequest;
import com.dreamtracker.app.habit.adapters.api.HabitRequest;
import com.dreamtracker.app.habit.adapters.api.HabitResponse;
import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.habit.domain.model.*;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.domain.ports.UserService;
import com.dreamtracker.app.view.domain.model.aggregate.StatsAggregator;
import jakarta.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
public class DomainHabitService implements HabitService {

  private final HabitRepositoryPort habitRepositoryPort;
  private final CurrentUserProvider currentUserProvider;
  private final UserService userService;
  private final CategoryRepositoryPort categoryRepositoryPort;
  private final HabitTrackRepositoryPort habitTrackRepositoryPort;
  private final StatsAggregator statsAggregator;
  private final GoalService goalService;
  private final HabitTrackService habitTrackService;
  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DomainGoalService.class);

 @Override
@Transactional
public boolean delete(UUID id) {
    var foundHabit = habitRepositoryPort.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    var goals = foundHabit.getGoals();

    List<UUID> goalUUIDs = new ArrayList<>();
    for (var goal : goals) {
        goalUUIDs.add(goal.getUuid());
    }

    for (UUID goalUUID : goalUUIDs) {
        goalService.delete(goalUUID);
    }

    habitRepositoryPort.deleteById(id);
    return true;
}

  @Override
  public List<HabitTrack> getHabitTrack(UUID id) {
    return habitTrackRepositoryPort.findByHabitUUID(id);
  }

  @Override
  @Transactional
  public HabitResponse createHabit(HabitRequest habitRequest) {

    var habitToCreate =
        Habit.builder()
            .name(habitRequest.name())
            .action(habitRequest.action())
            .duration(habitRequest.duration())
            .difficulty(habitRequest.difficulty())
            .status(HabitStatus.ACTIVE.toString())
            .categories(new ArrayList<>())
            .goals(new ArrayList<>())
            .userUUID(currentUserProvider.getCurrentUser())
            .build();

    var habitSavedToDB = habitRepositoryPort.save(habitToCreate);
    statsAggregator.initializeAggregates(habitSavedToDB.getId());
    return mapToResponse(habitSavedToDB);
  }

  @Override
  public Page<HabitResponse> getAllUserHabits() {
    var habits = habitRepositoryPort.findByUserUUID(currentUserProvider.getCurrentUser());
    var listOfHabitResponses = habits.stream().map(this::mapToResponse).toList();
    Page<HabitResponse> responsePage = new Page<>(listOfHabitResponses);
    return responsePage;
  }

  @Override
  public HabitResponse updateHabit(UUID id, HabitRequest habitRequest) {
    var habitToUpdate =
        habitRepositoryPort
            .findById(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    Optional.ofNullable(habitRequest.name()).ifPresent(habitToUpdate::setName);
    Optional.ofNullable(habitRequest.action()).ifPresent(habitToUpdate::setAction);
    Optional.ofNullable(habitRequest.frequency()).ifPresent(habitToUpdate::setFrequency);
    Optional.ofNullable(habitRequest.duration()).ifPresent(habitToUpdate::setDuration);
    Optional.ofNullable(habitRequest.difficulty()).ifPresent(habitToUpdate::setDifficulty);

    var updatedHabit = habitRepositoryPort.save(habitToUpdate);

    return mapToResponse(updatedHabit);
  }

  @Override
  @Transactional
  public void linkCategoryWithHabit(
      UUID habitId, HabitCategoryCreateRequest categoryCreateRequest) {
    var habitToLinkCategory =
        habitRepositoryPort
            .findById(habitId)
            .orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    var categoryToBeLinked =
        categoryRepositoryPort
            .findById(categoryCreateRequest.id())
            .orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    habitToLinkCategory.getCategories().add(categoryToBeLinked);
    categoryToBeLinked.getHabits().add(habitToLinkCategory);

    habitRepositoryPort.save(habitToLinkCategory);
    categoryRepositoryPort.save(categoryToBeLinked);
  }

  @Override
  public HabitResponse getHabitById(UUID habitUUID) {
    var foundHabit = habitRepositoryPort.findById(habitUUID).orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    return mapToResponse(foundHabit);
  }

  @Override
  @Async(value = "taskExecutor")
  public void manageHabitsBasedOnTheirStatus(LocalDate localDate) {
    var habits = habitRepositoryPort.findAll();
    var dayOfTheWeek = localDate.getDayOfWeek();
    var dayOfTheMonth = localDate.getDayOfMonth();

    manageHabitsByFrequency(habits, HabitFrequency.DAILY, "DAILY");

    if (dayOfTheWeek.equals(DayOfWeek.MONDAY)) {
      manageHabitsByFrequency(habits, HabitFrequency.WEEKLY, "WEEKLY");
    }

    if (dayOfTheMonth == 1) {
      manageHabitsByFrequency(habits, HabitFrequency.MONTHLY, "MONTHLY");
    }
  }

  private void manageHabitsByFrequency(List<Habit> habits, HabitFrequency frequency, String period) {
    habits.parallelStream()
        .filter(habit -> habit.getFrequency().equals(frequency.toString()))
        .forEach(habit -> manageHabit(habit, period));
  }


  private void manageHabit(Habit habit, String period) {
    logger.info("Managing habit: " + period);
    synchronized (habit) {
      try {
        switch (habit.getStatus()) {
          case "ACTIVE":
            habitTrackService.trackTheHabit(
                    new HabitTrackingRequest(habit.getId(), HabitTrackStatus.UNDONE.toString()));
            break;
          case "COOLDOWN":
            habit.setStatus(HabitStatus.ACTIVE.toString());
            break;
        }
        var habitSavedToDB = habitRepositoryPort.save(habit);
        logger.info(habitSavedToDB.toString());
      } catch (Exception e) {
        logger.error("Error managing habit: " + habit.getId(), e);
      }
    }
  }



  private HabitResponse mapToResponse(Habit habit) {
    return HabitResponse.builder()
        .id(habit.getId())
        .name(habit.getName())
        .action(habit.getAction())
        .duration(habit.getDuration())
        .difficulty(habit.getDifficulty())
        .status(habit.getStatus())
            .categories(habit.getCategories())
        .build();
  }
}
