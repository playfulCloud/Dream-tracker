package com.dreamtracker.app.habit.domain.ports;

import com.dreamtracker.app.habit.domain.model.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepositoryPort {
    Category save(Category category);
    Boolean existsById(UUID id);
    void deleteById(UUID id);
    Optional<Category>findById(UUID id);
    List<Category>findByUserUUID(UUID userUUID);
}
