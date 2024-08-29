package com.dreamtracker.app.user.adapters.userDb;

import com.dreamtracker.app.infrastructure.repository.SpringDataPositionRepository;
import com.dreamtracker.app.user.domain.model.Position;
import com.dreamtracker.app.user.domain.ports.PositionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class PostgresPositionRepository  implements PositionRepositoryPort {


    private final SpringDataPositionRepository springDataPositionRepository;


    @Override
    public Position save(Position position) {
        return springDataPositionRepository.save(position);
    }

    @Override
    public Optional<Position> findByUserUUID(UUID userUUID) {
        return springDataPositionRepository.findByUserUUID(userUUID);
    }
}
