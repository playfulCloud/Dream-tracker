package com.dreamtracker.app.view.adapters.viewDB;


import com.dreamtracker.app.infrastructure.repository.SpringDataViewRepository;
import com.dreamtracker.app.view.domain.model.View;
import com.dreamtracker.app.view.domain.ports.ViewRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PostgresViewRepository implements ViewRepositoryPort {

    private final SpringDataViewRepository  springDataViewRepository;

    @Override
    public View save(View view) {
        return springDataViewRepository.save(view);
    }

    @Override
    public Boolean existsById(UUID id) {
        return springDataViewRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        springDataViewRepository.deleteById(id);
    }

    @Override
    public Optional<View> findById(UUID id) {
        return springDataViewRepository.findById(id);
    }

    @Override
    public List<View> findByUserUUID(UUID userUUID) {
        return springDataViewRepository.findByUserUUID(userUUID);
    }
}
