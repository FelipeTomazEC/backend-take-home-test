package com.noom.interview.fullstack.sleep.application.ports.output;

import com.noom.interview.fullstack.sleep.domain.SleepQuality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Builder
@Getter
public class GetSleepSummaryOutput {
    public LocalTime averageSleepTime;
    public LocalTime averageBedTime;
    public LocalTime averageWakeUpTime;
    public Map<SleepQuality, Integer> sleepQualityFrequency;
    public Period period;

    @Getter
    @AllArgsConstructor
    public static class Period {
        LocalDate startDate;
        LocalDate endDate;
    }
}
