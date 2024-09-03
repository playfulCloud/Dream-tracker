package com.dreamtracker.app.user.adapters.userDb;

import com.dreamtracker.app.infrastructure.repository.SpringDataUserRepository;
import com.dreamtracker.app.user.domain.model.User;
import com.dreamtracker.app.user.domain.ports.UserRepositoryPort;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataUserRepository.findByEmail(email) ;
    }

    @Override
    public User getByResetToken(String resetToken) {
        return springDataUserRepository.getByResetToken(resetToken);
    }

    @Override
    public User getById(UUID uuid) {
        return springDataUserRepository.getById(uuid);
    }
}
