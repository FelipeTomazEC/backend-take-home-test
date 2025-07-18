package com.noom.interview.fullstack.sleep.infrastructure.controllers;

import com.noom.interview.fullstack.sleep.api.v1.SleepLogsApiV1;
import com.noom.interview.fullstack.sleep.api.v1.requests.CreateSleepLogHttpRequest;
import com.noom.interview.fullstack.sleep.api.v1.responses.GetSleepLogFromSpecificDateHttpResponse;
import com.noom.interview.fullstack.sleep.api.v1.responses.GetSleepSummaryHttpResponse;
import com.noom.interview.fullstack.sleep.application.ports.operations.CreateSleepLogOperation;
import com.noom.interview.fullstack.sleep.application.ports.operations.GetSleepLogFromSpecificDateOperation;
import com.noom.interview.fullstack.sleep.application.ports.operations.GetSleepSummaryOperation;
import com.noom.interview.fullstack.sleep.application.ports.output.GetSleepLogFromSpecificDateOutput;
import com.noom.interview.fullstack.sleep.application.ports.output.GetSleepSummaryOutput;
import com.noom.interview.fullstack.sleep.application.usecases.UseCase;
import com.noom.interview.fullstack.sleep.domain.SleepQuality;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.noom.interview.fullstack.sleep.api.v1.responses.GetSleepSummaryHttpResponse.Period;

@RestController
@RequiredArgsConstructor
public class SleepLogsHttpControllerV1 implements SleepLogsApiV1 {

    private final UseCase<CreateSleepLogOperation, Void> createSleepLogUseCase;
    private final UseCase<GetSleepLogFromSpecificDateOperation, GetSleepLogFromSpecificDateOutput> createSleepLogFromSpecificDateUseCase;
    private final UseCase<GetSleepSummaryOperation, GetSleepSummaryOutput> getSleepSummaryUseCase;

    @Override
    public void createSleepLog(UUID userId, CreateSleepLogHttpRequest request) {
        var operation = CreateSleepLogOperation.builder()
                .userId(userId)
                .bedTime(LocalDateTime.parse(request.getBedTimeAndDate()))
                .wakeUpTime(LocalDateTime.parse(request.getWakeUpTimeAndDate()))
                .quality(SleepQuality.valueOf(request.getQuality()))
                .build();

        createSleepLogUseCase.execute(operation);
    }

    @Override
    public GetSleepLogFromSpecificDateHttpResponse getSleepLogFromSpecificDate(UUID userId, String date) {
        var desiredDate = Optional.ofNullable(date)
                .map(LocalDate::parse)
                .orElse(LocalDate.now());

        var operation = GetSleepLogFromSpecificDateOperation.builder()
                .userId(userId)
                .date(desiredDate)
                .build();

        var output = createSleepLogFromSpecificDateUseCase.execute(operation);

        return GetSleepLogFromSpecificDateHttpResponse.builder()
                .totalSleepTime(output.getTotalSleepTime().toString())
                .bedTime(output.getBedTime().toString())
                .wakeUpTime(output.getWakeUpTime().toString())
                .quality(output.getSleepQuality().name())
                .date(output.getDate().toString())
                .build();
    }

    @Override
    public GetSleepSummaryHttpResponse getSleepSummary(UUID userId, String startDate, String endDate) {
        var endDateOrDefault = Optional.ofNullable(endDate)
                .map(LocalDate::parse)
                .orElse(LocalDate.now());

        var startDateOrDefault = Optional.ofNullable(startDate)
                .map(LocalDate::parse)
                .orElse(endDateOrDefault.minusDays(30));

        var operation = GetSleepSummaryOperation.builder()
                .userId(userId)
                .startDate(startDateOrDefault)
                .endDate(endDateOrDefault)
                .build();

        var output = getSleepSummaryUseCase.execute(operation);

        var sleepQualityFrequency = output.getSleepQualityFrequency().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().name(),
                        Map.Entry::getValue
                ));

        return GetSleepSummaryHttpResponse.builder()
                .averageSleepTime(output.getAverageSleepTime().toString())
                .averageBedTime(output.getAverageBedTime().toString())
                .averageWakeUpTime(output.getAverageWakeUpTime().toString())
                .sleepQualityFrequency(sleepQualityFrequency)
                .period(new Period(startDateOrDefault.toString(), endDateOrDefault.toString()))
                .build();
    }
}
