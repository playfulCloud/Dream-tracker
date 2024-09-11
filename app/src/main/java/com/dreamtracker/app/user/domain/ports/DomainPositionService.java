package com.dreamtracker.app.user.domain.ports;

import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.user.adapters.api.ActivationRequest;
import com.dreamtracker.app.user.adapters.api.PositionChangeRequest;
import com.dreamtracker.app.user.adapters.api.PositionRequest;
import com.dreamtracker.app.user.adapters.api.PositionResponse;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.domain.model.Position;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class DomainPositionService implements PositionService {

  private final PositionRepositoryPort positionRepositoryPort;
  private final CurrentUserProvider currentUserProvider;
  private static final org.slf4j.Logger logger =
      LoggerFactory.getLogger(DomainPositionService.class);

  @Override
  public PositionResponse create(PositionRequest positionRequest) {
    var position =
        Position.builder()
            .habitX(positionRequest.habitX())
            .habitY(positionRequest.habitY())
            .goalX(positionRequest.goalX())
            .goalY(positionRequest.goalY())
            .statX(positionRequest.statX())
            .statY(positionRequest.statY())
            .chartX(positionRequest.chartX())
            .chartY(positionRequest.chartY())
            .userUUID(currentUserProvider.getCurrentUser())
            .habitEnabled(positionRequest.habitEnabled())
            .goalEnabled(positionRequest.goalEnabled())
            .statsEnabled(positionRequest.statsEnabled())
            .chartsEnabled(positionRequest.chartsEnabled())
            .build();
    logger.info(position.toString());
    var positionSavedToDB = positionRepositoryPort.save(position);
    return mapToResponse(positionSavedToDB);
  }

  @Override
  public PositionResponse changePosition(PositionChangeRequest positionChangeRequest) {
    var positionToUpdate =
        positionRepositoryPort
            .findByUserUUID(currentUserProvider.getCurrentUser())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    logger.debug(positionToUpdate.toString());
    logger.debug(positionChangeRequest.toString());

    Optional.ofNullable(positionChangeRequest.habitX()).ifPresent(positionToUpdate::setHabitX);
    Optional.ofNullable(positionChangeRequest.habitY()).ifPresent(positionToUpdate::setHabitY);
    Optional.ofNullable(positionChangeRequest.goalX()).ifPresent(positionToUpdate::setGoalX);
    Optional.ofNullable(positionChangeRequest.goalY()).ifPresent(positionToUpdate::setGoalY);
    Optional.ofNullable(positionChangeRequest.statX()).ifPresent(positionToUpdate::setStatX);
    Optional.ofNullable(positionChangeRequest.statY()).ifPresent(positionToUpdate::setStatY);
    Optional.ofNullable(positionChangeRequest.chartX()).ifPresent(positionToUpdate::setChartX);
    Optional.ofNullable(positionChangeRequest.chartY()).ifPresent(positionToUpdate::setChartY);

    logger.info(positionToUpdate.toString());

    var updatedPosition = positionRepositoryPort.save(positionToUpdate);

    return mapToResponse(updatedPosition);
  }

  @Override
  public PositionResponse changeActivation(ActivationRequest activationRequest) {
    var positionToUpdate =
            positionRepositoryPort
                    .findByUserUUID(currentUserProvider.getCurrentUser())
                    .orElseGet(() -> {
                      Position newPosition = Position.builder()
                              .habitX(0)
                              .habitY(0)
                              .goalX(0)
                              .goalY(0)
                              .statX(0)
                              .statY(0)
                              .chartX(0)
                              .chartY(0)
                              .userUUID(currentUserProvider.getCurrentUser())
                              .habitEnabled(activationRequest.habitEnabled())
                              .goalEnabled(activationRequest.goalEnabled())
                              .statsEnabled(activationRequest.statsEnabled())
                              .chartsEnabled(activationRequest.chartsEnabled())
                              .build();
                      return positionRepositoryPort.save(newPosition);
                    });

    logger.debug(positionToUpdate.toString());
    logger.debug(activationRequest.toString());

    Optional.ofNullable(activationRequest.habitEnabled())
            .ifPresent(positionToUpdate::setHabitEnabled);
    Optional.ofNullable(activationRequest.goalEnabled())
            .ifPresent(positionToUpdate::setGoalEnabled);
    Optional.ofNullable(activationRequest.statsEnabled())
            .ifPresent(positionToUpdate::setStatsEnabled);
    Optional.ofNullable(activationRequest.chartsEnabled())
            .ifPresent(positionToUpdate::setChartsEnabled);

    logger.info(positionToUpdate.toString());

    var updatedPosition = positionRepositoryPort.save(positionToUpdate);

    return mapToResponse(updatedPosition);
  }


  @Override
  public PositionResponse findByUserUUID(UUID userUUID) {
    var position =
        positionRepositoryPort
            .findByUserUUID(userUUID)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    return mapToResponse(position);
  }

  @Override
  public boolean deleteUser(UUID userUUID) {
    positionRepositoryPort.deleteByUserUUID(userUUID);
    return true;
  }

  private PositionResponse mapToResponse(Position position) {
    return new PositionResponse(
        position.getHabitX(),
        position.getHabitY(),
        position.getGoalX(),
        position.getGoalY(),
        position.getStatX(),
        position.getStatY(),
        position.getChartX(),
        position.getChartY(),
        position.isHabitEnabled(),
        position.isGoalEnabled(),
        position.isStatsEnabled(),
        position.isChartsEnabled());
  }
}
