package org.vinevweb.cardiohristov.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.vinevweb.cardiohristov.common.Constants.PROCEDURE_CREATION_ERROR;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = PROCEDURE_CREATION_ERROR)
public class ProcedureCreateFailureException extends RuntimeException {



    public ProcedureCreateFailureException(String message) {
        super(message);
    }
}
