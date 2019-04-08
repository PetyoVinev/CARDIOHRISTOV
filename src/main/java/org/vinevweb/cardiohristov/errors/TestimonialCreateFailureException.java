package org.vinevweb.cardiohristov.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.vinevweb.cardiohristov.common.Constants.TESTIMONIAL_CREATION_ERROR;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = TESTIMONIAL_CREATION_ERROR)
public class TestimonialCreateFailureException extends RuntimeException {



    public TestimonialCreateFailureException(String message) {
        super(message);
    }
}
