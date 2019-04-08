package org.vinevweb.cardiohristov.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.vinevweb.cardiohristov.common.Constants.DUPLICATE_USER_NAME_ERROR;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = DUPLICATE_USER_NAME_ERROR)
public class UserAlreadyExistsException extends RuntimeException {


    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
