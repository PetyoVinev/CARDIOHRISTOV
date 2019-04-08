package org.vinevweb.cardiohristov.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.vinevweb.cardiohristov.common.Constants.APPOINTMENT_CREATION_ERROR;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = APPOINTMENT_CREATION_ERROR)
public class AppointmentCreateFailureException extends RuntimeException {

    public AppointmentCreateFailureException(String message) {
        super(message);
    }
}
