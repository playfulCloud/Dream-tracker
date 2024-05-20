package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.habit.domain.fixtures.HabitFixture;
import com.dreamtracker.app.habit.domain.fixtures.HabitTrackFixture;
import com.dreamtracker.app.habit.domain.model.Habit;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SpringDataHabitTrackRepositoryTest implements HabitTrackFixture , HabitFixture {


    CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
    @Autowired
    SpringDataHabitTrackRepository springDataHabitTrackRepository;
    Habit habitToAssignATrack;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:alpine");

    @Test
    void connectionEstablished(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @BeforeEach
    void setUp(){
        habitToAssignATrack = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
        var habitTrack = getSampleHabitTrack(habitToAssignATrack.getId(), String.valueOf(LocalDate.now())).build();

        springDataHabitTrackRepository.save(habitTrack);
    }


    @Test
    void getListOfUserHabitPositiveTestCase(){
        var habitTrackList = springDataHabitTrackRepository.findByHabitUUID(habitToAssignATrack.getId());
        assertThat(habitTrackList).isNotNull();
        assertThat(habitTrackList.size()).isNotEqualTo(0);
    }


}
