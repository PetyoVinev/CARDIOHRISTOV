package org.vinevweb.cardiohristov.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error occurred during article creation.")
public class ArticleCreateFailureException extends RuntimeException {

    public ArticleCreateFailureException(String message) {
        super(message);
    }
}
