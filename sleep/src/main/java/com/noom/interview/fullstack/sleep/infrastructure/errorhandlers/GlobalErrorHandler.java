package com.noom.interview.fullstack.sleep.infrastructure.errorhandlers;

import com.noom.interview.fullstack.sleep.api.v1.responses.ErrorsHttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ErrorsHttpResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        var errors = exception.getFieldErrors()
                .stream()
                .map(o -> String.format("Field '%s' is invalid. Details: %s", o.getField(),  o.getDefaultMessage()))
                .collect(Collectors.toSet());

        return new ErrorsHttpResponse(errors);
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = MissingRequestHeaderException.class)
    public ErrorsHttpResponse handleMissingHeaderException(MissingRequestHeaderException exception) {
        var errorMessage = String.format("%s header is required. It must be a valid UUID.", exception.getHeaderName());

        return new ErrorsHttpResponse(Set.of(errorMessage));
    }
}
