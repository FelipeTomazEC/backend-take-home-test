package com.noom.interview.fullstack.sleep.application.ports.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class GetSleepLogFromSpecificDateCommand {
    private UUID userId;
    private LocalDate date;
}
