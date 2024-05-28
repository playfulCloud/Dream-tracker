package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.habit.domain.model.Category;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByUserUUID(UUID id);
}
