package com.dreamtracker.app.user.domain.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.dreamtracker.app.infrastructure.exception.CredentialsValidationException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import org.junit.jupiter.api.Test;

class CredentialsValidatorTest {

  CredentialsValidator credentialsValidator = new CredentialsValidator();

  @Test
  void validateEmailPositiveTestCase() {
    assertTrue(credentialsValidator.validateEmail("test@test.com"));
  }

  @Test
  void validateEmailDomenAbsentCredentialsValidationExceptionThrown() {
    assertThatThrownBy(() -> credentialsValidator.validateEmail("test@test"))
        .isInstanceOf(CredentialsValidationException.class)
        .hasMessage(ExceptionMessages.credentialsValidationExceptionMessageForMail);
  }

  @Test
  void validateEmailSubDomenAbsentCredentialsValidationExceptionThrown() {
    assertThatThrownBy(() -> credentialsValidator.validateEmail("test@.com"))
        .isInstanceOf(CredentialsValidationException.class)
        .hasMessage(ExceptionMessages.credentialsValidationExceptionMessageForMail);
  }

  @Test
  void validateAtAbsentCredentialsValidationExceptionThrown() {
    assertThatThrownBy(() -> credentialsValidator.validateEmail("test.com"))
        .isInstanceOf(CredentialsValidationException.class)
        .hasMessage(ExceptionMessages.credentialsValidationExceptionMessageForMail);
  }

  @Test
  void validateUserNameAbsentCredentialsValidationExceptionThrown() {
    assertThatThrownBy(() -> credentialsValidator.validateEmail("@test.com"))
        .isInstanceOf(CredentialsValidationException.class)
        .hasMessage(ExceptionMessages.credentialsValidationExceptionMessageForMail);
  }

  @Test
  void validatePasswordPositiveTestCase() {
    assertTrue(credentialsValidator.validatePassword("Valid1@Password"));
  }

  @Test
  void validatePasswordNoUpperCaseCredentialsValidationExceptionThrown() {
    assertThatThrownBy(() -> credentialsValidator.validatePassword("valid1@password"))
        .isInstanceOf(CredentialsValidationException.class)
        .hasMessage(ExceptionMessages.credentialsValidationExceptionMessageForPassword);
  }

  @Test
  void validatePasswordNoLowerCaseCredentialsValidationExceptionThrown() {
    assertThatThrownBy(() -> credentialsValidator.validatePassword("VALID1@PASSWORD"))
        .isInstanceOf(CredentialsValidationException.class)
        .hasMessage(ExceptionMessages.credentialsValidationExceptionMessageForPassword);
  }

  @Test
  void validatePasswordNoDigitCredentialsValidationExceptionThrown() {
    assertThatThrownBy(() -> credentialsValidator.validatePassword("Valid@Password"))
        .isInstanceOf(CredentialsValidationException.class)
        .hasMessage(ExceptionMessages.credentialsValidationExceptionMessageForPassword);
  }

  @Test
  void validatePasswordNoSpecialCharacterCredentialsValidationExceptionThrown() {
    assertThatThrownBy(() -> credentialsValidator.validatePassword("Valid1Password"))
        .isInstanceOf(CredentialsValidationException.class)
        .hasMessage(ExceptionMessages.credentialsValidationExceptionMessageForPassword);
  }

  @Test
  void validatePasswordTooShortCredentialsValidationExceptionThrown() {
    assertThatThrownBy(() -> credentialsValidator.validatePassword("V1@a"))
        .isInstanceOf(CredentialsValidationException.class)
        .hasMessage(ExceptionMessages.credentialsValidationExceptionMessageForPassword);
  }

  @Test
  void validatePasswordTooLongCredentialsValidationExceptionThrown() {
    assertThatThrownBy(
            () -> credentialsValidator.validatePassword("Valid1@PasswordValid1@Password"))
        .isInstanceOf(CredentialsValidationException.class)
        .hasMessage(ExceptionMessages.credentialsValidationExceptionMessageForPassword);
  }
}
