package org.vinevweb.cardiohristov.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.vinevweb.cardiohristov.common.Constants.COMMENT_CREATION_ERROR;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = COMMENT_CREATION_ERROR)
public class CommentCreateFailureException extends RuntimeException {


    public CommentCreateFailureException(String message) {
        super(message);
    }
}
