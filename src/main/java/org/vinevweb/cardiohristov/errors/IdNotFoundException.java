package org.vinevweb.cardiohristov.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.vinevweb.cardiohristov.common.Constants.NON_EXISTENT_ID;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = NON_EXISTENT_ID)
public class IdNotFoundException extends RuntimeException {



    public IdNotFoundException(String message) {
        super(message);
    }
}
