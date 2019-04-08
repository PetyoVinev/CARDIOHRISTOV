package org.vinevweb.cardiohristov.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.vinevweb.cardiohristov.common.Constants.USER_REGISTRATION_ERROR;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = USER_REGISTRATION_ERROR)
public class UserRegisterFailureException extends RuntimeException {

    public UserRegisterFailureException(String message) {
        super(message);
    }
}
