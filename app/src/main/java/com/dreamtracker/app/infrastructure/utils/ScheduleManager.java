package com.dreamtracker.app.infrastructure.utils;

import com.dreamtracker.app.goal.domain.ports.GoalRepositoryPort;
import com.dreamtracker.app.goal.domain.ports.GoalService;
import com.dreamtracker.app.habit.domain.ports.HabitRepositoryPort;
import com.dreamtracker.app.habit.domain.ports.HabitService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ScheduleManager {

  private static final Logger log = LoggerFactory.getLogger(ScheduleManager.class);
  private final HabitService habitService;
  private final GoalService goalService;


  @Scheduled(cron = "0 0 0 * * ?")
  public void manageHabitsBasedOnTheirStatus(){
    var currentDate = LocalDate.now();
    log.info("Starting to manage habits based on their status" + currentDate);
    habitService.manageHabitsBasedOnTheirStatus(currentDate);
  }
}
