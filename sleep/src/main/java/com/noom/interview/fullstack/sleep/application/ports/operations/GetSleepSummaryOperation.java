package com.noom.interview.fullstack.sleep.application.ports.operations;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
public class GetSleepSummaryOperation {
    LocalDate startDate;
    LocalDate endDate;
    UUID userId;
}
