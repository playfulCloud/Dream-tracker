package com.dreamtracker.app.user.adapters.userDb;

import com.dreamtracker.app.infrastructure.repository.SpringDataUserRepository;
import com.dreamtracker.app.user.domain.model.User;
import com.dreamtracker.app.user.domain.ports.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class PostgresUserRepository implements UserRepositoryPort {

    private final SpringDataUserRepository springDataUserRepository;

    @Override
    public User save(User user) {
        return springDataUserRepository.save(user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return springDataUserRepository.findById(id);
    }
}
