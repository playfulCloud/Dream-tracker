package com.dreamtracker.app.infrastructure.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.dreamtracker.app.configuration.TestPostgresConfiguration;
import com.dreamtracker.app.habit.domain.fixtures.CategoryFixtures;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
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
class SpringDataCategoryRepositoryTest implements CategoryFixtures {


    CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();
    @Autowired
    SpringDataCategoryRepository springDataCategoryRepository;

    @Autowired
    PostgreSQLContainer<?> postgreSQLContainer;

    @Test
    void connectionEstablished(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @BeforeEach
    void setUp(){
        var categoryWithUser = getSampleCategoryBuilder(currentUserProvider.getCurrentUser()).build();
        springDataCategoryRepository.save(categoryWithUser);
    }


    @Test
    void getListOfUserHabitPositiveTestCase(){
        var userCategory = springDataCategoryRepository.findByUserUUID(currentUserProvider.getCurrentUser());
        assertThat(userCategory).isNotNull();
        assertThat(userCategory.size()).isNotEqualTo(0);
    }


}
