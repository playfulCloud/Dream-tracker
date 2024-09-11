package com.dreamtracker.app.habit.domain.ports;

import com.dreamtracker.app.goal.domain.model.Goal;
import com.dreamtracker.app.goal.domain.ports.DomainGoalService;
import com.dreamtracker.app.goal.domain.ports.GoalRepositoryPort;
import com.dreamtracker.app.goal.domain.ports.GoalService;
import com.dreamtracker.app.habit.adapters.api.HabitTrackResponse;
import com.dreamtracker.app.habit.adapters.api.HabitTrackingRequest;
import com.dreamtracker.app.habit.domain.model.ChartResponse;
import com.dreamtracker.app.habit.domain.model.HabitStatus;
import com.dreamtracker.app.habit.domain.model.HabitTrack;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.infrastructure.utils.DateService;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.view.domain.model.aggregate.StatsAggregator;

import java.time.*;
import java.time.format.DateTimeFormatter;
import jakarta.transaction.Transactional;
import java.time.*;
import java.util.*;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@Data
public class DomainHabitTrackService implements HabitTrackService {

  private final HabitTrackRepositoryPort habitTrackRepositoryPort;
  private final HabitRepositoryPort habitRepositoryPort;
  private final StatsAggregator statsAggregator;
  private final Clock clock;
  private final GoalService domainGoalService;
  private final DateService dateService;
  private final CurrentUserProvider currentUserProvider;
  private static final Logger logger = LoggerFactory.getLogger(DomainGoalService.class);


  @Override
  public Page<HabitTrackResponse> getAllTracksOfHabit(UUID id) {
    var listOfTracks = habitTrackRepositoryPort.findByHabitUUID(id);
    var listOfTracksResponses = listOfTracks.stream().map(this::mapToResponse).toList();
    Page<HabitTrackResponse> habitTrackResponsePage = new Page<>(listOfTracksResponses);
    return habitTrackResponsePage;
  }

  @Override
  @Transactional
  public HabitTrackResponse trackTheHabit(HabitTrackingRequest habitTrackingRequest) {

    var habitToUpdateTracking =
        habitRepositoryPort
            .findById(habitTrackingRequest.habitId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    var actualDate = Instant.now(clock);
    logger.debug(habitToUpdateTracking.toString());



    var track =
        HabitTrack.builder()
            .date(actualDate)
            .status(habitTrackingRequest.status())
            .habitUUID(habitToUpdateTracking.getId())
            .build();


    var trackSavedToDB = habitTrackRepositoryPort.save(track);
    var habitTrackResponse = mapToResponse(trackSavedToDB);

    statsAggregator.requestStatsUpdated(habitToUpdateTracking.getId(), habitTrackResponse);

    if (trackSavedToDB.getStatus().equals("DONE")) {
      var cooldown = dateService.getCooldownPeriodBasedOnCurrentDate(actualDate, habitToUpdateTracking.getFrequency());
      habitToUpdateTracking.setCoolDownTill(cooldown);
      habitRepositoryPort.save(habitToUpdateTracking);
      updateGoalProgress(habitToUpdateTracking.getGoals());
    }
    getChartsFromHabitTracks();
    return habitTrackResponse;
  }

  private HabitTrackResponse mapToResponse(HabitTrack habitTrack) {
    return HabitTrackResponse.builder()
        .date(habitTrack.getDate())
        .status(habitTrack.getStatus())
        .build();
  }

  private void updateGoalProgress(List<Goal> goals) {
    List<Goal> goalsCopy = new ArrayList<>(goals);
    for (Goal goal : goalsCopy) {
      domainGoalService.increaseCompletionCount(goal.getUuid());
    }
  }

  @Override
  public Page<ChartResponse>getChartsFromHabitTracks(){
    List<HabitTrack> tracks =  habitTrackRepositoryPort.findAllByUserUUID(currentUserProvider.getCurrentUser());
    HashMap<LocalDate, Integer> countsPerDay = new HashMap<>();
    HashSet<UUID>habitsCounts = new HashSet<>();
    for(HabitTrack track : tracks){
      habitsCounts.add(track.getHabitUUID());
      var dayDate = track.getDate().atZone(ZoneId.systemDefault()).toLocalDate();
      if(countsPerDay.containsKey(dayDate)){
          countsPerDay.put(dayDate,countsPerDay.get(dayDate)+1);
      }else{
        countsPerDay.put(dayDate,1);
      }
    }
    List<ChartResponse> items = new ArrayList<>();
    for(Map.Entry<LocalDate,Integer> iter : countsPerDay.entrySet()){
        items.add(new ChartResponse(iter.getKey(),iter.getValue(),habitsCounts.size()));
    }
    logger.info(items.toString());
    return new Page<>(items);
  }


  private static boolean areInstantsOnSameDay(Instant instant1, Instant instant2) {
    LocalDate date1 = instant1.atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate date2 = instant2.atZone(ZoneId.systemDefault()).toLocalDate();

    return date1.equals(date2);
  }




}
