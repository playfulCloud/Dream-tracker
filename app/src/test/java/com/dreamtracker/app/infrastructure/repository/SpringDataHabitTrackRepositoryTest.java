package com.dreamtracker.app.infrastructure.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.dreamtracker.app.configuration.TestPostgresConfiguration;
import com.dreamtracker.app.fixtures.HabitFixture;
import com.dreamtracker.app.fixtures.HabitTrackFixture;
import com.dreamtracker.app.habit.domain.model.Habit;
import com.dreamtracker.app.infrastructure.utils.DateService;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = TestPostgresConfiguration.class)
class SpringDataHabitTrackRepositoryTest implements HabitTrackFixture, HabitFixture {

    CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
    @Autowired
    SpringDataHabitTrackRepository springDataHabitTrackRepository;
    Habit habitToAssignATrack;

  @Autowired PostgreSQLContainer<?> postgreSQLContainer;
    @Autowired
    private DateService dateService;

    @Test
    void connectionEstablished(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @BeforeEach
    void setUp(){
        habitToAssignATrack = getSampleHabitBuilder(currentUserProvider.getCurrentFromSecurityContext()).build();
        var habitTrack = getSampleHabitTrack(habitToAssignATrack.getId(), dateService.getCurrentDateInISO8601()).build();

        springDataHabitTrackRepository.save(habitTrack);
    }


    @Test
    void getListOfUserHabitPositiveTestCase(){
        var habitTrackList = springDataHabitTrackRepository.findByHabitUUID(habitToAssignATrack.getId());
        assertThat(habitTrackList).isNotNull();
        assertThat(habitTrackList.size()).isNotEqualTo(0);
    }


}
