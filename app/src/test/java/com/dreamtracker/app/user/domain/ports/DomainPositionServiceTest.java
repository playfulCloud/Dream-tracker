package com.dreamtracker.app.user.domain.ports;

import com.dreamtracker.app.fixtures.PositionFixtures;
import com.dreamtracker.app.goal.domain.ports.DomainGoalService;
import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class DomainPositionServiceTest implements PositionFixtures {

    private final PositionRepositoryPort positionRepositoryPort =
            Mockito.mock(PositionRepositoryPort.class);
    private final CurrentUserProvider currentUserProvider = Mockito.mock(CurrentUserProvider.class);
    private final DomainPositionService domainPositionService =
            new DomainPositionService(positionRepositoryPort, currentUserProvider);
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DomainGoalService.class);

    @Test
    void createPositionPositiveTestCase() {
        // given
        var request = getPositionRequest().build();
        var position =
                getPosition().userUUID(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e")).build();

        when(currentUserProvider.getCurrentUser())
                .thenReturn(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"));
        when(positionRepositoryPort.save(position)).thenReturn(position);
        var expected = getPositionResponse().build();
        logger.info(request.toString());
        logger.info(expected.toString());
        // when
        var actual = domainPositionService.create(request);
        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateOnlyElementActivationChanged() {
        // given
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
        // when
        var actual = domainPositionService.changeActivation(activationRequest);
        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateOnlyElementsPositionChanged() {
        // given
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

        // when
        var actual = domainPositionService.changePosition(changeRequest);
        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateOnlyElementActivationChangedNewPositionCreated() {
        // given
        var activationRequest = getActivationRequest().build();
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
        var expected = getPositionResponse().chartsEnabled(false).habitEnabled(false).build();
        logger.info(expected.toString());
        // when
        var actual = domainPositionService.changeActivation(activationRequest);
        // then
        assertThat(expected).isEqualTo(actual);

    }

    @Test
    void updateOnlyElementsPositionChangedEntityExceptionThrown() {
        // given
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

        assertThatThrownBy(
                // when
                () -> domainPositionService.changePosition(changeRequest)
                // then
        ).isInstanceOf(EntityNotFoundException.class).hasMessage(ExceptionMessages.entityNotFoundExceptionMessage);
    }

}
