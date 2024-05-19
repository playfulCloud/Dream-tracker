package com.dreamtracker.app.infrastructure.config;

import com.dreamtracker.app.habit.domain.ports.*;
import com.dreamtracker.app.infrastructure.repository.CategoryRepository;
import com.dreamtracker.app.infrastructure.repository.HabitTrackRepository;
import com.dreamtracker.app.infrastructure.repository.SpringDataHabitRepository;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.domain.ports.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

  @Bean
  HabitService habitService(
      HabitRepositoryPort habitRepositoryPort,
      CurrentUserProvider currentUserProvider,
      UserService userService,
      CategoryRepository categoryRepository,
      HabitTrackRepository habitTrackRepository) {
    return new DomainHabitService(
            habitRepositoryPort,
        currentUserProvider,
        userService,
        categoryRepository,
        habitTrackRepository);
  }
}
