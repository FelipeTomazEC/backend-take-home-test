package com.noom.interview.fullstack.sleep.infrastructure.controllers;

import com.noom.interview.fullstack.sleep.api.v1.SleepLogsApiV1;
import com.noom.interview.fullstack.sleep.api.v1.requests.CreateSleepLogHttpRequest;
import com.noom.interview.fullstack.sleep.application.ports.commands.CreateSleepLogCommand;
import com.noom.interview.fullstack.sleep.application.usecases.UseCase;
import com.noom.interview.fullstack.sleep.domain.SleepQuality;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SleepLogsHttpControllerV1 implements SleepLogsApiV1 {

    private final UseCase<CreateSleepLogCommand, Void> useCase;

    @Override
    public void createSleepLog(UUID userId, CreateSleepLogHttpRequest request) {
        var command = CreateSleepLogCommand.builder()
                .userId(userId)
                .bedTime(LocalDateTime.parse(request.getBedTimeAndDate()))
                .wakeUpTime(LocalDateTime.parse(request.getWakeUpTimeAndDate()))
                .quality(SleepQuality.valueOf(request.getQuality()))
                .build();

        useCase.execute(command);
    }
}
