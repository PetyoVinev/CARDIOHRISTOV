package org.vinevweb.cardiohristov.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error occurred during appointment creation.")
public class AppointmentCreateFailureException extends RuntimeException {

    public AppointmentCreateFailureException(String message) {
        super(message);
    }
}
