package com.noom.interview.fullstack.sleep.application.services;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class AverageTimeCalculator {
    public LocalTime calculateAvgTime(List<LocalTime> times) {
        var averageInSeconds = times.stream()
                .mapToLong(LocalTime::toSecondOfDay)
                .average()
                .orElse(0L);

        return LocalTime.ofSecondOfDay((long) averageInSeconds);
    }
}