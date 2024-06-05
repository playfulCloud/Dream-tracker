package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.view.domain.model.View;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataViewRepository extends JpaRepository<View, UUID> {
    List<View> findByUserUUID(UUID id);
}
