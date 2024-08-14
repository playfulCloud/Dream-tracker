package com.dreamtracker.app.user.domain.model;

import com.dreamtracker.app.infrastructure.exception.CredentialsValidationException;
import com.dreamtracker.app.infrastructure.exception.ExceptionMessages;
import org.springframework.stereotype.Component;

@Component
public class CredentialsValidator {

  public boolean validateEmail(String email) {

    boolean matches = email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    if (!matches) {
      throw new CredentialsValidationException(ExceptionMessages.credentialsValidationExceptionMessageForMail);
    }
    return true;
  }

  public boolean validatePassword(String password) {
      boolean matches = password.matches("^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,20}$");
      if (!matches) {
          throw new CredentialsValidationException(ExceptionMessages.credentialsValidationExceptionMessageForPassword);
      }
      return true;
  }
}
