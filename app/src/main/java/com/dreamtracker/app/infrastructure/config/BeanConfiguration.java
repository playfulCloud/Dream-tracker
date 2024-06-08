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
import java.time.Clock;

import com.dreamtracker.app.view.domain.model.aggregateManagers.StatsAggregator;
import com.dreamtracker.app.view.domain.ports.DomainViewService;
import com.dreamtracker.app.view.domain.ports.ViewRepositoryPort;
import com.dreamtracker.app.view.domain.ports.ViewService;
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
          HabitTrackRepositoryPort habitTrackRepositoryPort, StatsAggregator statsAggregator) {
    return new DomainHabitService(
        habitRepositoryPort,
        currentUserProvider,
        userService,
        categoryRepositoryPort,
        habitTrackRepositoryPort,statsAggregator);
  }

  @Bean
  ViewService viewService(ViewRepositoryPort viewRepositoryPort,StatsAggregator statsAggregator){
    return new DomainViewService(viewRepositoryPort,statsAggregator);
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
  public GoalService goalService(GoalRepositoryPort goalRepositoryPort, SpringDataUserRepository springDataUserRepository, CurrentUserProvider currentUserProvider, HabitRepositoryPort habitRepositoryPort){
   return new DomainGoalService(goalRepositoryPort, springDataUserRepository,currentUserProvider,habitRepositoryPort);
  }

  @Bean
  public UserService userService(CurrentUserProvider currentUserProvider, UserRepositoryPort userRepositoryPort){
      return new DomainUserService(userRepositoryPort,currentUserProvider);
  }
}
