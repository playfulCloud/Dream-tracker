package com.dreamtracker.app.infrastructure.auth;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class PasswordResetTokenGenerator {

    public static String generateResetToken(String email) {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] randomBytes = new byte[24];
            secureRandom.nextBytes(randomBytes);

            String tokenData = email + "-" + System.currentTimeMillis() + "-" + Base64.getEncoder().encodeToString(randomBytes);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(tokenData.getBytes());

            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error occured during token generation", e);
        }
    }
}
