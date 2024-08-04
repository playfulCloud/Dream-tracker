package com.dreamtracker.app.infrastructure.utils;

import com.dreamtracker.app.goal.domain.ports.GoalService;
import com.dreamtracker.app.habit.domain.ports.HabitService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

  @Scheduled(cron = "0 1 0 * * ?")
  public void manageGoalsBasedOnTheirStatus() {
    var currentDate = LocalDate.now();
    log.info("Starting to manage Goals based on their status" + currentDate);
    goalService.markGoalAsFailedIfNotCompleted();
  }
}
