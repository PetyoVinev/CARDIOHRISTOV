package org.vinevweb.cardiohristov.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.vinevweb.cardiohristov.common.Constants.ARTICLE_CREATION_ERROR;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = ARTICLE_CREATION_ERROR)
public class ArticleCreateFailureException extends RuntimeException {

    public ArticleCreateFailureException(String message) {
        super(message);
    }
}
