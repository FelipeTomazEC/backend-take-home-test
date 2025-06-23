package com.noom.interview.fullstack.sleep.domain.errors;

import java.time.LocalDate;

public class SleepLogAlreadyExistsException extends RuntimeException {
    public SleepLogAlreadyExistsException(LocalDate date) {
        super(String.format("Sleep log already exists for date: %s", date));
    }
}
