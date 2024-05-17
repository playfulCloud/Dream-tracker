package com.dreamtracker.app.infrastructure.security;

import java.util.UUID;

public interface CurrentUserProvider {
    UUID getCurrentUser();
}
