package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.configuration.TestPostgresConfiguration;
import com.dreamtracker.app.habit.adapters.habitDb.PostgresHabitRepository;
import com.dreamtracker.app.habit.domain.fixtures.HabitFixture;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = TestPostgresConfiguration.class)
class SpringDataHabitRepositoryTest implements HabitFixture {

    CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
    @Autowired
    SpringDataHabitRepository springDataHabitRepository;

    @Autowired
    PostgreSQLContainer<?> postgreSQLContainer;

   @Test
   void connectionEstablished(){
     assertThat(postgreSQLContainer.isCreated()).isTrue();
     assertThat(postgreSQLContainer.isRunning()).isTrue();
   }

   @BeforeEach
    void setUp(){
       var habitWithUser = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
       springDataHabitRepository.save(habitWithUser);
   }


    @Test
    void getListOfUserHabitPositiveTestCase(){
       var userHabits = springDataHabitRepository.findByUserUUID(currentUserProvider.getCurrentUser());
       assertThat(userHabits).isNotNull();
       assertThat(userHabits.size()).isNotEqualTo(0);
    }


}
