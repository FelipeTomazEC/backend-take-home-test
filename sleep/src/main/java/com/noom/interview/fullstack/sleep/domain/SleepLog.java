package com.noom.interview.fullstack.sleep.domain;

import com.noom.interview.fullstack.sleep.domain.errors.InvalidBedTimeIntervalException;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;


@Builder
public class SleepLog {
    private final LocalDateTime bedTime;
    private final LocalDateTime wakeUpTime;
    private final SleepQuality quality;

    public SleepLog(
            LocalDateTime timeToBed,
            LocalDateTime wakeUpTime,
            SleepQuality quality
    ) {
        if (timeToBed.isAfter(wakeUpTime)) {
            throw new InvalidBedTimeIntervalException();
        }

        this.bedTime = timeToBed;
        this.wakeUpTime = wakeUpTime;
        this.quality = quality;
    }

    public LocalTime getTotalSleepTimeInBed() {
        var endTimeInSeconds = wakeUpTime.toEpochSecond(ZoneOffset.UTC);
        var startTimeInSeconds = bedTime.toEpochSecond(ZoneOffset.UTC);
        var totalSleepTimeInSeconds = endTimeInSeconds - startTimeInSeconds;

        return LocalTime.ofSecondOfDay(totalSleepTimeInSeconds);
    }

    public LocalDate getSleepDate() {
        return wakeUpTime.toLocalDate();
    }
}
