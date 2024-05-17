package com.dreamtracker.app.domain.repository;

import com.dreamtracker.app.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByUserUUID(UUID id);
}
