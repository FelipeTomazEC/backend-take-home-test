package com.noom.interview.fullstack.sleep.infrastructure.controllers;

import com.noom.interview.fullstack.sleep.api.v1.SleepLogsApiV1;
import com.noom.interview.fullstack.sleep.api.v1.requests.CreateSleepLogHttpRequest;
import com.noom.interview.fullstack.sleep.api.v1.responses.GetSleepLogFromSpecificDateHttpResponse;
import com.noom.interview.fullstack.sleep.application.ports.commands.CreateSleepLogCommand;
import com.noom.interview.fullstack.sleep.application.ports.commands.GetSleepLogFromSpecificDateCommand;
import com.noom.interview.fullstack.sleep.application.ports.output.GetSleepLogFromSpecificDateOutput;
import com.noom.interview.fullstack.sleep.application.usecases.UseCase;
import com.noom.interview.fullstack.sleep.domain.SleepQuality;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SleepLogsHttpControllerV1 implements SleepLogsApiV1 {

    private final UseCase<CreateSleepLogCommand, Void> createSleepLogUseCase;
    private final UseCase<GetSleepLogFromSpecificDateCommand, GetSleepLogFromSpecificDateOutput> createSleepLogFromSpecificDateUseCase;

    @Override
    public void createSleepLog(UUID userId, CreateSleepLogHttpRequest request) {
        var command = CreateSleepLogCommand.builder()
                .userId(userId)
                .bedTime(LocalDateTime.parse(request.getBedTimeAndDate()))
                .wakeUpTime(LocalDateTime.parse(request.getWakeUpTimeAndDate()))
                .quality(SleepQuality.valueOf(request.getQuality()))
                .build();

        createSleepLogUseCase.execute(command);
    }

    @Override
    public GetSleepLogFromSpecificDateHttpResponse getSleepLogFromSpecificDate(UUID userId, String date) {
        var desiredDate = Optional.ofNullable(date)
                .map(LocalDate::parse)
                .orElse(LocalDate.now());

        var command = GetSleepLogFromSpecificDateCommand.builder()
                .userId(userId)
                .date(desiredDate)
                .build();

        var output = createSleepLogFromSpecificDateUseCase.execute(command);

        return GetSleepLogFromSpecificDateHttpResponse.builder()
                .totalSleepTime(output.getTotalSleepTime().toString())
                .bedTime(output.getBedTime().toString())
                .wakeUpTime(output.getWakeUpTime().toString())
                .quality(output.getSleepQuality().name())
                .date(output.getDate().toString())
                .build();
    }
}
