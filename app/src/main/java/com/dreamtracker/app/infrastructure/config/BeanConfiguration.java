package com.dreamtracker.app.infrastructure.config;

import com.dreamtracker.app.goal.domain.ports.DomainGoalService;
import com.dreamtracker.app.goal.domain.ports.GoalRepositoryPort;
import com.dreamtracker.app.goal.domain.ports.GoalService;
import com.dreamtracker.app.habit.domain.ports.*;
import com.dreamtracker.app.infrastructure.repository.SpringDataUserRepository;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.domain.ports.DomainUserService;
import com.dreamtracker.app.user.domain.ports.UserRepositoryPort;
import com.dreamtracker.app.user.domain.ports.UserService;
import com.dreamtracker.app.view.domain.model.aggregate.StatsAggregator;
import com.dreamtracker.app.view.domain.ports.DomainViewService;
import com.dreamtracker.app.view.domain.ports.ViewRepositoryPort;
import com.dreamtracker.app.view.domain.ports.ViewService;
import java.time.Clock;

import org.apache.logging.log4j.Logger;
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
          HabitTrackRepositoryPort habitTrackRepositoryPort, StatsAggregator statsAggregator,GoalService domainGoalService) {
    return new DomainHabitService(
        habitRepositoryPort,
        currentUserProvider,
        userService,
        categoryRepositoryPort,
        habitTrackRepositoryPort,statsAggregator, domainGoalService);
  }

  @Bean
  ViewService viewService(ViewRepositoryPort viewRepositoryPort,StatsAggregator statsAggregator){
    return new DomainViewService(viewRepositoryPort,statsAggregator);
  }

  @Bean
  HabitTrackService habitTrackService(
      HabitTrackRepositoryPort habitTrackRepositoryPort,
      HabitRepositoryPort habitRepositoryPort,
      StatsAggregator statsAggregator,
      Clock clock, GoalService domainGoalService) {
    return new DomainHabitTrackService(
        habitTrackRepositoryPort, habitRepositoryPort, statsAggregator, clock, domainGoalService);
  }

  @Bean
  CategoryService categoryService(CategoryRepositoryPort categoryRepositoryPort,UserService userService, CurrentUserProvider currentUserProvider, HabitRepositoryPort habitRepositoryPort){
    return new DomainCategoryService(categoryRepositoryPort,userService,currentUserProvider, habitRepositoryPort);
  }

  @Bean
  public GoalService goalService(GoalRepositoryPort goalRepositoryPort, SpringDataUserRepository springDataUserRepository, CurrentUserProvider currentUserProvider, HabitRepositoryPort habitRepositoryPort){
   return new DomainGoalService(goalRepositoryPort, springDataUserRepository,currentUserProvider,habitRepositoryPort);
  }

  @Bean
  public UserService userService(CurrentUserProvider currentUserProvider, UserRepositoryPort userRepositoryPort){
      return new DomainUserService(userRepositoryPort,currentUserProvider);
  }
}
