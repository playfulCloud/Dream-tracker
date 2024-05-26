package com.dreamtracker.app.user.config;

import java.util.UUID;

public interface CurrentUserProvider {
    UUID getCurrentUser();
}
