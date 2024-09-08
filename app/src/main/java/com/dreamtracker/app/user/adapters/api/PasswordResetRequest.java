package com.dreamtracker.app.user.adapters.api;

public record PasswordResetRequest(String password,String passwordConfirmation,String resetToken) {
}
