package com.dreamtracker.app.view.domain.ports;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.dreamtracker.app.fixtures.HabitFixture;
import com.dreamtracker.app.fixtures.ViewFixture;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.view.domain.model.aggregate.StatsAggregator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DomainViewServiceTest implements ViewFixture, HabitFixture {

  private final ViewRepositoryPort viewRepositoryPort = Mockito.mock(ViewRepositoryPort.class);
  private final DomainViewService domainViewService =
      new DomainViewService(viewRepositoryPort);
  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();

  @Test
  void createViewPositiveTestCase() {
    // given
    var view = getViewBuilder().id(null).userUUID(currentUserProvider.getCurrentFromSecurityContext()).build();
    var viewSavedToDB = getViewBuilder().userUUID(currentUserProvider.getCurrentFromSecurityContext()).build();
    var viewRequest = getViewRequestBuilder().build();
    var expectedResponse =
        getViewResponseBuilder().userUUID(currentUserProvider.getCurrentFromSecurityContext()).build();
    when(viewRepositoryPort.save(view)).thenReturn(viewSavedToDB);
    // when
    var actualResponse = domainViewService.createView(viewRequest);
    // then
    assertThat(actualResponse).isEqualTo(expectedResponse);
  }

  @Test
  void deleteViewPositiveTestCase() {
    // given
    var view = getViewBuilder().userUUID(currentUserProvider.getCurrentFromSecurityContext()).build();
    when(viewRepositoryPort.existsById(view.getId())).thenReturn(true);
    var expectedResponse = true;
    // when
    var actualResponse = domainViewService.deleteView(view.getId());
    // then
    assertThat(actualResponse).isEqualTo(expectedResponse);
  }

  @Test
  void deleteViewNegativeTestCase() {
    // given
    var view = getViewBuilder().userUUID(currentUserProvider.getCurrentFromSecurityContext()).build();
    when(viewRepositoryPort.existsById(view.getId())).thenReturn(false);
    var expectedResponse = false;
    // when
    var actualResponse = domainViewService.deleteView(view.getId());
    // then
    assertThat(actualResponse).isEqualTo(expectedResponse);
  }

  @Test
  void getViewByNamePositiveTestCase() {
    // given
    var view = getViewBuilder().userUUID(currentUserProvider.getCurrentFromSecurityContext()).build();
    var expectedResponse =
        getViewResponseBuilder().userUUID(currentUserProvider.getCurrentFromSecurityContext()).build();
    when(viewRepositoryPort.findByName(view.getName())).thenReturn(Optional.of(view));
    // when
    var actualResponse = domainViewService.getViewByName(view.getName());
    // then
    assertThat(actualResponse).isEqualTo(expectedResponse);
  }

  @Test
  void updateViewPositiveTestCase() {
    // given
    var view = getViewBuilder().userUUID(currentUserProvider.getCurrentFromSecurityContext()).build();
    var viewRequest =
        getViewRequestBuilder().id(view.getId()).stats(false).description("confirmed").build();
    var expectedResponse =
        getViewResponseBuilder()
            .userUUID(currentUserProvider.getCurrentFromSecurityContext())
            .stats(false)
            .description("confirmed")
            .build();
    when(viewRepositoryPort.findById(view.getId())).thenReturn(Optional.of(view));
    when(viewRepositoryPort.save(view)).thenReturn(view);
    // when
    var actualResponse = domainViewService.updatedView(viewRequest);
    // then
    assertThat(actualResponse).isEqualTo(expectedResponse);
  }

  @Test
  void updateViewEntityNotFoundExceptionThrown() {
    // given
    var view = getViewBuilder().userUUID(currentUserProvider.getCurrentFromSecurityContext()).build();
    var viewRequest =
        getViewRequestBuilder().id(view.getId()).stats(false).description("confirmed").build();
    when(viewRepositoryPort.findById(view.getId())).thenReturn(Optional.empty());
    when(viewRepositoryPort.save(view)).thenReturn(view);
    // when
    assertThatThrownBy(
            () -> {
              domainViewService.updatedView(viewRequest);
              // then
            })
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }

  @Test
  void getViewByNameEntityNotFound() {
    // given
    var view = getViewBuilder().userUUID(currentUserProvider.getCurrentFromSecurityContext()).build();
    when(viewRepositoryPort.findByName(view.getName())).thenReturn(Optional.empty());
    // when
    assertThatThrownBy(
            () -> {
              domainViewService.getViewByName(view.getName());
              // then
            })
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }
}
