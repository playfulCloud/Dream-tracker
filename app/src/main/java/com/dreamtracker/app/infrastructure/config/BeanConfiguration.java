package com.dreamtracker.app.infrastructure.config;

import com.dreamtracker.app.goal.domain.ports.DomainGoalService;
import com.dreamtracker.app.goal.domain.ports.GoalRepositoryPort;
import com.dreamtracker.app.goal.domain.ports.GoalService;
import com.dreamtracker.app.habit.domain.ports.*;
import com.dreamtracker.app.infrastructure.mail.MailService;
import com.dreamtracker.app.infrastructure.repository.SpringDataUserRepository;
import com.dreamtracker.app.infrastructure.utils.DateService;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.domain.model.Position;
import com.dreamtracker.app.user.domain.ports.*;
import com.dreamtracker.app.view.domain.model.aggregate.StatsAggregator;
import com.dreamtracker.app.view.domain.ports.DomainViewService;
import com.dreamtracker.app.view.domain.ports.ViewRepositoryPort;
import com.dreamtracker.app.view.domain.ports.ViewService;
import java.time.Clock;

import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfiguration {

  @Bean
  HabitService habitService(
          HabitRepositoryPort habitRepositoryPort,
          CurrentUserProvider currentUserProvider,
          UserService userService,
          CategoryRepositoryPort categoryRepositoryPort,
          HabitTrackRepositoryPort habitTrackRepositoryPort, StatsAggregator statsAggregator,GoalService domainGoalService, HabitTrackService habitTrackService, Clock clock, DateService dateService) {
    return new DomainHabitService(
        habitRepositoryPort,
        currentUserProvider,
        userService,
        categoryRepositoryPort,
        habitTrackRepositoryPort,statsAggregator, domainGoalService, habitTrackService, clock,dateService);
  }

  @Bean
  ViewService viewService(ViewRepositoryPort viewRepositoryPort){
    return new DomainViewService(viewRepositoryPort);
  }

  @Bean
  HabitTrackService habitTrackService(
          HabitTrackRepositoryPort habitTrackRepositoryPort,
          HabitRepositoryPort habitRepositoryPort,
          StatsAggregator statsAggregator,
          Clock clock, GoalService domainGoalService, DateService dateService, CurrentUserProvider currentUserProvider) {
    return new DomainHabitTrackService(
        habitTrackRepositoryPort, habitRepositoryPort, statsAggregator, clock, domainGoalService, dateService, currentUserProvider);
  }

  @Bean
  CategoryService categoryService(CategoryRepositoryPort categoryRepositoryPort,UserService userService, CurrentUserProvider currentUserProvider, HabitRepositoryPort habitRepositoryPort){
    return new DomainCategoryService(categoryRepositoryPort,userService,currentUserProvider, habitRepositoryPort);
  }

  @Bean
  public GoalService goalService(GoalRepositoryPort goalRepositoryPort, SpringDataUserRepository springDataUserRepository, CurrentUserProvider currentUserProvider, HabitRepositoryPort habitRepositoryPort, Clock clock){
   return new DomainGoalService(goalRepositoryPort, springDataUserRepository,currentUserProvider,habitRepositoryPort,clock);
  }

  @Bean
  public UserService userService(UserRepositoryPort userRepositoryPort, CurrentUserProvider currentUserProvider, MailService mailService, PasswordEncoder passwordEncoder){
      return new DomainUserService(userRepositoryPort,currentUserProvider,mailService,passwordEncoder);
  }


  @Bean
  public PositionService positionService( PositionRepositoryPort positionRepositoryPort,CurrentUserProvider currentUserProvider){
    return new DomainPositionService(positionRepositoryPort,currentUserProvider);
  }


}
