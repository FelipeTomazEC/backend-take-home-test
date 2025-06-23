package com.noom.interview.fullstack.sleep.application.ports.commands;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
public class GetSleepSummaryCommand {
    LocalDate startDate;
    LocalDate endDate;
    UUID userId;
}
