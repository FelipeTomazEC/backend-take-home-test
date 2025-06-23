package com.noom.interview.fullstack.sleep.infrastructure.errorhandlers;

import com.noom.interview.fullstack.sleep.api.v1.responses.ErrorsHttpResponse;
import com.noom.interview.fullstack.sleep.domain.errors.InvalidBedTimeIntervalException;
import com.noom.interview.fullstack.sleep.domain.errors.NoLogsForThisDateException;
import com.noom.interview.fullstack.sleep.domain.errors.SleepLogAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
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

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NoLogsForThisDateException.class)
    public ErrorsHttpResponse handleNoLogsForThisDateException(NoLogsForThisDateException exception) {
        return new ErrorsHttpResponse(Set.of(exception.getMessage()));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ErrorsHttpResponse handleConstraintViolationException(ConstraintViolationException exception) {
        var errors = exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        return new ErrorsHttpResponse(errors);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(value = SleepLogAlreadyExistsException.class)
    public ErrorsHttpResponse handleSleepLogAlreadyExistsException(SleepLogAlreadyExistsException exception) {
        return new ErrorsHttpResponse(Set.of(exception.getMessage()));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = InvalidBedTimeIntervalException.class)
    public ErrorsHttpResponse  handleInvalidBedTimeIntervalException(InvalidBedTimeIntervalException exception) {
        return new ErrorsHttpResponse(Set.of(exception.getMessage()));
    }
}
