package org.vinevweb.cardiohristov.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error occurred during procedure creation.")
public class ProcedureCreateFailureException extends RuntimeException {

    public ProcedureCreateFailureException(String message) {
        super(message);
    }
}
