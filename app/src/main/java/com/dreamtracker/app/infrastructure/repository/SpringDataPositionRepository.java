package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.user.domain.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataPositionRepository extends JpaRepository<Position, UUID> {
    Optional<Position> findByUserUUID(UUID userUUID);
}
