package com.noom.interview.fullstack.sleep.application.ports.output;

import com.noom.interview.fullstack.sleep.domain.SleepQuality;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class GetSleepLogFromSpecificDateOutput {
    private LocalDate date;
    private LocalTime totalSleepTime;
    private LocalTime bedTime;
    private LocalTime wakeUpTime;
    private SleepQuality sleepQuality;
}
