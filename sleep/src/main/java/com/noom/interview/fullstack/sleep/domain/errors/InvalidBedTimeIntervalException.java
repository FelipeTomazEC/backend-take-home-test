package com.noom.interview.fullstack.sleep.domain.errors;

public class InvalidBedTimeIntervalException extends RuntimeException {
    public InvalidBedTimeIntervalException() {
        super("Bed time must be before wake up time.");
    }
}
