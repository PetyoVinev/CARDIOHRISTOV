package org.vinevweb.cardiohristov.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error occurred during testimonial creation.")
public class TestimonialCreateFailureException extends RuntimeException {

    public TestimonialCreateFailureException(String message) {
        super(message);
    }
}
