package com.noom.interview.fullstack.sleep.application.ports.commands;

import com.noom.interview.fullstack.sleep.domain.SleepQuality;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class CreateSleepLogCommand {
    private LocalDateTime bedTime;
    private LocalDateTime wakeUpTime;
    private SleepQuality quality;
    private UUID userId;
}
