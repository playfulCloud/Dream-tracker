package com.dreamtracker.app.user.config;

import jakarta.servlet.http.HttpSession;

import java.util.UUID;

public interface CurrentUserProvider  {
    UUID getCurrentUser();
}
