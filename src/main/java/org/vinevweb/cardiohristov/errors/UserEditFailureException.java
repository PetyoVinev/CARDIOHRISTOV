package org.vinevweb.cardiohristov.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.vinevweb.cardiohristov.common.Constants.USER_PROFILE_EDIT_ERROR;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = USER_PROFILE_EDIT_ERROR)
public class UserEditFailureException extends RuntimeException {

    public UserEditFailureException(String message) {
        super(message);
    }
}
