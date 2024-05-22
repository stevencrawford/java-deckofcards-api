package com.example.deckofcards.api.common.exception;

import com.example.deckofcards.api.common.response.RestResponseError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {

    private final RestResponseError.Error error;

    public ApplicationException(final HttpStatus httpStatus, final String code, final String description) {
        super();
        error = new RestResponseError.Error(httpStatus, code, description);
    }
}
