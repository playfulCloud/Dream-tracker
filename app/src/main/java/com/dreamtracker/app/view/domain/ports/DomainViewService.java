package com.dreamtracker.app.view.domain.ports;

import com.dreamtracker.app.infrastructure.exception.EntityNotFoundException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import com.dreamtracker.app.user.config.CurrentUserProvider;
import com.dreamtracker.app.user.config.MockCurrentUserProviderImpl;
import com.dreamtracker.app.view.adapters.api.ViewRequest;
import com.dreamtracker.app.view.adapters.api.ViewResponse;
import com.dreamtracker.app.view.domain.model.View;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class DomainViewService implements ViewService {

  private final ViewRepositoryPort viewRepositoryPort;
  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DomainViewService.class);
  private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();

  @Override
  public ViewResponse createView(ViewRequest viewRequest) {
    var viewToSave =
        View.builder()
            .name(viewRequest.name())
            .stats(viewRequest.stats())
            .goals(viewRequest.goals())
            .habits(viewRequest.habits())
            .userUUID(currentUserProvider.getCurrentUser())
            .description(viewRequest.description())
            .build();

    var viewSavedToDB = viewRepositoryPort.save(viewToSave);
    logger.trace(viewSavedToDB.toString());
    return mapToResponse(viewSavedToDB);
  }

  @Override
  public boolean deleteView(UUID viewUUID) {
    if (viewRepositoryPort.existsById(viewUUID)) {
      viewRepositoryPort.deleteById(viewUUID);
      return true;
    }
    return false;
  }

  @Override
  public ViewResponse getViewByName(String name) {
    var view =
        viewRepositoryPort
            .findByName(name)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));
    return mapToResponse(view);
  }

  @Override
  public ViewResponse updatedView(ViewRequest viewRequest) {
    var viewToUpdate =
        viewRepositoryPort
            .findById(viewRequest.id())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(ExceptionMessages.entityNotFoundExceptionMessage));

    Optional.ofNullable(viewRequest.name()).ifPresent(viewToUpdate::setName);
    Optional.ofNullable(viewRequest.description()).ifPresent(viewToUpdate::setDescription);
    Optional.ofNullable(viewRequest.habits()).ifPresent(viewToUpdate::setHabits);
    Optional.ofNullable(viewRequest.stats()).ifPresent(viewToUpdate::setStats);
    Optional.ofNullable(viewRequest.goals()).ifPresent(viewToUpdate::setGoals);

    var updatedView = viewRepositoryPort.save(viewToUpdate);

    return mapToResponse(updatedView);
  }

  private ViewResponse mapToResponse(View view) {
    return ViewResponse.builder()
        .id(view.getId())
        .name(view.getName())
        .description(view.getDescription())
        .userUUID(view.getUserUUID())
        .habits(view.isHabits())
        .goals(view.isGoals())
        .stats(view.isStats())
        .build();
  }
}
