package org.vinevweb.cardiohristov.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error occurred during comment creation.")
public class CommentCreateFailureException extends RuntimeException {

    public CommentCreateFailureException(String message) {
        super(message);
    }
}
