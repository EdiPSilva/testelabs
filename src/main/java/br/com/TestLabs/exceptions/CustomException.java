package br.com.TestLabs.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private final HttpStatus httpStatus;

    public CustomException(final HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public CustomException(final String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public CustomException(final String message, final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
