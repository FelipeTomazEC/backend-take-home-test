package com.noom.interview.fullstack.sleep.domain.errors;

import java.time.LocalDate;

public class NoLogsForThisDateException extends RuntimeException {
    public NoLogsForThisDateException(LocalDate date) {
        super(
            String.format("No sleep logs found for the date: %s", date)
        );
    }
}
