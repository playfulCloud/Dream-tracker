package com.dreamtracker.app.user.domain.ports;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.dreamtracker.app.fixtures.PositionFixtures;
import com.dreamtracker.app.goal.domain.ports.DomainGoalService;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;

class DomainPositionServiceTest implements PositionFixtures {

  private final PositionRepositoryPort positionRepositoryPort =
      Mockito.mock(PositionRepositoryPort.class);
  private final CurrentUserProvider currentUserProvider = Mockito.mock(CurrentUserProvider.class);
  private final DomainPositionService domainPositionService =
      new DomainPositionService(positionRepositoryPort, currentUserProvider);
  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DomainGoalService.class);

  @Test
  void createPositionPositiveTestCase() {
    var request = getPositionRequest().build();
    var position =
        getPosition().userUUID(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")).build();

    when(currentUserProvider.getCurrentUser())
        .thenReturn(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"));
    when(positionRepositoryPort.save(position)).thenReturn(position);
    var expected = getPositionResponse().build();
    logger.info(request.toString());
    logger.info(expected.toString());

    var actual = domainPositionService.create(request);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateOnlyElementActivationChanged() {
    var activationRequest = getActivationRequest().chartsEnabled(true).habitEnabled(true).build();
    var position =
        getPosition()
            .id(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"))
            .userUUID(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"))
            .build();


    logger.info(position.toString());

    when(currentUserProvider.getCurrentUser())
            .thenReturn(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"));
    when(positionRepositoryPort.findByUserUUID(
            UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")))
        .thenReturn(Optional.of(position));
    when(positionRepositoryPort.save(position)).thenReturn(position);
    var expected = getPositionResponse().chartsEnabled(true).habitEnabled(true).build();
    logger.info(expected.toString());

    var actual = domainPositionService.changeActivation(activationRequest);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateOnlyElementsPositionChanged() {
    var changeRequest = getPositionChangeRequest().goalX(10).goalY(15).build();
    var position =
            getPosition()
                    .id(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"))
                    .userUUID(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"))
                    .build();


    logger.info(position.toString());

    when(currentUserProvider.getCurrentUser())
            .thenReturn(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"));
    when(positionRepositoryPort.findByUserUUID(
            UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")))
            .thenReturn(Optional.of(position));
    when(positionRepositoryPort.save(position)).thenReturn(position);
    var expected = getPositionResponse().goalX(10).goalY(15).build();

    var actual = domainPositionService.changePosition(changeRequest);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateOnlyElementActivationChangedEntityNotFoundExceptionThrown() {
    var activationRequest = getActivationRequest().chartsEnabled(true).habitEnabled(true).build();
    var position =
            getPosition()
                    .id(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"))
                    .userUUID(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"))
                    .build();


    logger.info(position.toString());

    when(currentUserProvider.getCurrentUser())
            .thenReturn(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"));
    when(positionRepositoryPort.findByUserUUID(
            UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")))
            .thenReturn(Optional.empty());
    when(positionRepositoryPort.save(position)).thenReturn(position);
    var expected = getPositionResponse().chartsEnabled(true).habitEnabled(true).build();
    logger.info(expected.toString());

    assertThatThrownBy(
            () ->domainPositionService.changeActivation(activationRequest)
    ).isInstanceOf(EntityNotFoundException.class).hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }

  @Test
  void updateOnlyElementsPositionChangedEntityExceptionThrown() {
    var changeRequest = getPositionChangeRequest().goalX(10).goalY(15).build();
    var position =
            getPosition()
                    .id(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"))
                    .userUUID(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"))
                    .build();


    logger.info(position.toString());

    when(currentUserProvider.getCurrentUser())
            .thenReturn(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"));
    when(positionRepositoryPort.findByUserUUID(
            UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")))
            .thenReturn(Optional.empty());
    when(positionRepositoryPort.save(position)).thenReturn(position);
    var expected = getPositionResponse().goalX(10).goalY(15).build();

    assertThatThrownBy(
            () ->domainPositionService.changePosition(changeRequest)
    ).isInstanceOf(EntityNotFoundException.class).hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
  }

}
