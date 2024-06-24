package com.dreamtracker.app.view.domain.ports;


import com.dreamtracker.app.view.domain.model.View;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ViewRepositoryPort {
    View save(View view);
    Boolean existsById(UUID id);
    void deleteById(UUID id);
    Optional<View> findById(UUID id);
    List<View> findByUserUUID(UUID userUUID);
    Optional<View>findByName(String name);
}
