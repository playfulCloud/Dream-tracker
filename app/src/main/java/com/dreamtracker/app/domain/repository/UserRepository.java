package com.dreamtracker.app.domain.repository;

import com.dreamtracker.app.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {}
