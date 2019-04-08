package org.vinevweb.cardiohristov.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.vinevweb.cardiohristov.common.Constants.NON_EXISTENT_NAME;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = NON_EXISTENT_NAME)
public class NameNotFoundException extends RuntimeException {



    public NameNotFoundException(String message) {
        super(message);
    }
}
