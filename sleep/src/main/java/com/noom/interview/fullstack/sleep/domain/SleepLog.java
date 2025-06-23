package com.noom.interview.fullstack.sleep.domain;

import com.noom.interview.fullstack.sleep.domain.errors.InvalidBedTimeIntervalException;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;


@Builder
public class SleepLog {
    private final LocalDateTime bedTime;
    private final LocalDateTime wakeUpTime;

    @Getter
    private final SleepQuality quality;

    public SleepLog(
            LocalDateTime timeToBed,
            LocalDateTime wakeUpTime,
            SleepQuality quality
    ) {
        if (timeToBed.isAfter(wakeUpTime)) {
            throw new InvalidBedTimeIntervalException("Bed time cannot be after wake up time.");
        }

        if (wakeUpTime.isAfter(LocalDateTime.now())) {
            throw new InvalidBedTimeIntervalException("Wake up time cannot be in the future.");
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

    public LocalTime getBedTime() {
        return bedTime.toLocalTime();
    }

    public LocalTime getWakeUpTime() {
        return wakeUpTime.toLocalTime();
    }
}
