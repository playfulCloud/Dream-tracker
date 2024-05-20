package com.dreamtracker.app.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestPostgresConfiguration {

  @Bean
  public PostgreSQLContainer<?> postgreSQLContainer() {
    PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:alpine");
    container.start();
    return container;
  }
}
