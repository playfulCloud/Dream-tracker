package com.dreamtracker.app.user.domain.ports;

import com.dreamtracker.app.user.domain.model.Position;

import java.util.Optional;
import java.util.UUID;

public interface PositionRepositoryPort {
   Position save(Position position);
   Optional<Position> findByUserUUID(UUID userUUID);
}
