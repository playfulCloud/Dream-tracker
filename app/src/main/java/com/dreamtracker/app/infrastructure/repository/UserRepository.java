package com.dreamtracker.app.infrastructure.repository;

import com.dreamtracker.app.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {}
