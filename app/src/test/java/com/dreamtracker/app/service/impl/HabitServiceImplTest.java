package com.dreamtracker.app.service.impl;

import com.dreamtracker.app.entity.User;
import com.dreamtracker.app.fixtures.HabitFixture;
import com.dreamtracker.app.fixtures.HabitTrackFixture;
import com.dreamtracker.app.fixtures.UserFixtures;
import com.dreamtracker.app.repository.CategoryRepository;
import com.dreamtracker.app.repository.HabitRepository;
import com.dreamtracker.app.repository.HabitTrackRepository;
import com.dreamtracker.app.security.CurrentUserProvider;
import com.dreamtracker.app.service.HabitService;
import com.dreamtracker.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class HabitServiceImplTest implements HabitFixture, HabitTrackFixture, UserFixtures {

    private final HabitRepository habitRepository = Mockito.mock(HabitRepository.class);
    private final HabitTrackRepository habitTrackRepository = Mockito.mock(HabitTrackRepository.class);
    private final UserService userService = Mockito.mock(UserService.class);
   private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
   private final CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
   private  HabitService habitService;
   private User sampleUser;
   private Clock fixedClock;


    @BeforeEach
    void setUp(){

        fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        sampleUser = getSampleUser(currentUserProvider.getCurrentUser()).build();
        habitService = new HabitServiceImpl(habitRepository,currentUserProvider,userService,categoryRepository,habitTrackRepository);
    }




    @Test
    void findHabitById() {
      }



    @Test
    void deletePositiveTestCase() {
        var sampleHabit = getSampleHabitBuilder(sampleUser.getUuid()).build();
        var expectedOutput = true;
        when(habitRepository.existsById(sampleHabit.getId())).thenReturn(expectedOutput);
        var actualOutput = habitService.delete(sampleHabit.getId());
        assertThat(actualOutput).isEqualTo(expectedOutput);
    }

    @Test
    void deleteNegativeTestCase() {
        var sampleHabit = getSampleHabitBuilder(sampleUser.getUuid()).build();
        var expectedOutput = false;
        when(habitRepository.existsById(sampleHabit.getId())).thenReturn(expectedOutput);
        var actualOutput = habitService.delete(sampleHabit.getId());
        assertThat(actualOutput).isEqualTo(expectedOutput);
    }

      @Test
    void getHabitTrack() {
      }

    @Test
    void createHabitPositiveTestCase() {
        var sampleHabit = getSampleHabitBuilder(sampleUser.getUuid()).build();
        var sampleHabitTrack = getSampleHabitTrack(sampleHabit.getId(),ZonedDateTime.now(fixedClock).format(DateTimeFormatter.ISO_DATE_TIME));
       when()
    }

    @Test
    void createHabitEmptyList() {

    }

    @Test
    void getAllUserHabits() {
      }

    @Test
    void updateHabit() {
      }

    @Test
    void linkCategoryWithHabit() {
      }
}