package com.noom.interview.fullstack.sleep.application.ports.output;

import com.noom.interview.fullstack.sleep.domain.SleepQuality;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;
import java.util.Map;

@Builder
@Getter
public class GetSleepSummaryOutput {
    private LocalTime averageSleepTime;
    private LocalTime averageBedTime;
    private LocalTime averageWakeUpTime;
    private Map<SleepQuality, Integer> sleepQualityFrequency;
}
