package com.dreamtracker.app.infrastructure.config;

import com.dreamtracker.app.goal.domain.ports.DomainGoalService;
import com.dreamtracker.app.goal.domain.ports.GoalRepositoryPort;
import com.dreamtracker.app.goal.domain.ports.GoalService;
import com.dreamtracker.app.habit.domain.ports.*;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.domain.ports.UserService;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

  @Bean
  HabitService habitService(
      HabitRepositoryPort habitRepositoryPort,
      CurrentUserProvider currentUserProvider,
      UserService userService,
      CategoryRepositoryPort categoryRepositoryPort,
      HabitTrackRepositoryPort habitTrackRepositoryPort) {
    return new DomainHabitService(
        habitRepositoryPort,
        currentUserProvider,
        userService,
        categoryRepositoryPort,
        habitTrackRepositoryPort);
  }

  @Bean
  HabitTrackService habitTrackService(
      HabitTrackRepositoryPort habitTrackRepositoryPort,
      HabitRepositoryPort habitRepositoryPort,
      Clock clock) {
    return new DomainHabitTrackService(habitTrackRepositoryPort, habitRepositoryPort, clock);
  }

  @Bean
  CategoryService categoryService(CategoryRepositoryPort categoryRepositoryPort,UserService userService, CurrentUserProvider currentUserProvider){
    return new DomainCategoryService(categoryRepositoryPort,userService,currentUserProvider);
  }

  @Bean
  public GoalService goalService(GoalRepositoryPort goalRepositoryPort, UserService userService, CurrentUserProvider currentUserProvider, HabitRepositoryPort habitRepositoryPort){
   return new DomainGoalService(goalRepositoryPort,userService,currentUserProvider,habitRepositoryPort);
  }
}
