package com.noom.interview.fullstack.sleep.api.v1.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class GetSleepSummaryHttpResponse {
    public String averageSleepTime;
    public String averageBedTime;
    public String averageWakeUpTime;
    public Map<String, Integer> sleepQualityFrequency;
    public Period period;

    @Getter
    @AllArgsConstructor
    public static class Period {
        String startDate;
        String endDate;
    }
}
