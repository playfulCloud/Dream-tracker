package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.habit.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface SpringDataCategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByUserUUID(UUID id);
}
