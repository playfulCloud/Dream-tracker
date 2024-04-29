package com.dreamtracker.app.security;

import java.util.UUID;

public interface CurrentUserProvider {
    UUID getCurrentUser();
}
