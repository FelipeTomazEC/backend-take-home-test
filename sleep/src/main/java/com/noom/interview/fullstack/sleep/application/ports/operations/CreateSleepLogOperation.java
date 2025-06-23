package com.noom.interview.fullstack.sleep.application.ports.operations;

import com.noom.interview.fullstack.sleep.domain.SleepQuality;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class CreateSleepLogOperation {
    private LocalDateTime bedTime;
    private LocalDateTime wakeUpTime;
    private SleepQuality quality;
    private UUID userId;
}
