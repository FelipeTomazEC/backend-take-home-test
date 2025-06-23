package com.noom.interview.fullstack.sleep.domain.errors;

public class InvalidBedTimeIntervalException extends RuntimeException {
    public InvalidBedTimeIntervalException(String reason) {
        super(reason);
    }
}
