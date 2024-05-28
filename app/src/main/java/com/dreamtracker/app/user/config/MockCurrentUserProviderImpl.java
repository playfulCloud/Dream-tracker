package com.dreamtracker.app.user.config;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class MockCurrentUserProviderImpl implements CurrentUserProvider {

    private static final UUID TEMP_USER_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @Override
    public UUID getCurrentUser() {
        return TEMP_USER_UUID;
    }
}
