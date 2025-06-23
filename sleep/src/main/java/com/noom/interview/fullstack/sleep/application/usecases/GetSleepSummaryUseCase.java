package com.noom.interview.fullstack.sleep.application.usecases;

import com.noom.interview.fullstack.sleep.application.ports.commands.GetSleepSummaryCommand;
import com.noom.interview.fullstack.sleep.application.ports.output.GetSleepSummaryOutput;
import com.noom.interview.fullstack.sleep.application.ports.repositories.GetLogsFromPeriodRepository;
import com.noom.interview.fullstack.sleep.application.services.AverageTimeCalculator;
import com.noom.interview.fullstack.sleep.domain.SleepLog;
import com.noom.interview.fullstack.sleep.domain.SleepQuality;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.noom.interview.fullstack.sleep.application.ports.output.GetSleepSummaryOutput.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetSleepSummaryUseCase implements
        UseCase<GetSleepSummaryCommand, GetSleepSummaryOutput>
{

    private final GetLogsFromPeriodRepository getLogsFromPeriodRepository;
    private final AverageTimeCalculator averageTimeCalculator;

    @Override
    public GetSleepSummaryOutput execute(GetSleepSummaryCommand command) {
        log.info("Retrieving sleep summary: userId={}, from={}, to={}",
                 command.getUserId(), command.getStartDate(), command.getEndDate());

        var sleepLogs = getLogsFromPeriodRepository.findByPeriod(
                command.getStartDate(),
                command.getEndDate(),
                command.getUserId()
        );

        var allSleepTimes = sleepLogs.stream()
                .map(SleepLog::getTotalSleepTimeInBed)
                .collect(Collectors.toList());

        var allBedTimes = sleepLogs.stream()
                .map(SleepLog::getBedTime)
                .collect(Collectors.toList());

        var allWakeUpTimes = sleepLogs.stream()
                .map(SleepLog::getWakeUpTime)
                .collect(Collectors.toList());

        var allSleepQualities = sleepLogs.stream()
                .map(SleepLog::getQuality)
                .collect(Collectors.toList());


        return GetSleepSummaryOutput.builder()
                .averageBedTime(averageTimeCalculator.calculateAvgTime(allBedTimes))
                .averageWakeUpTime(averageTimeCalculator.calculateAvgTime(allWakeUpTimes))
                .averageSleepTime(averageTimeCalculator.calculateAvgTime(allSleepTimes))
                .sleepQualityFrequency(aggregateSleepQualityCounts(allSleepQualities))
                .period(new Period(command.getStartDate(), command.getEndDate()))
                .build();
    }

    private Map<SleepQuality, Integer> aggregateSleepQualityCounts(List<SleepQuality> sleepQualities) {
        var sleepQualityAggregation = sleepQualities.stream()
                .collect(Collectors.groupingBy(
                        quality -> quality,
                        Collectors.summingInt(quality -> 1)
                ));

        var qualitiesNotPresentInLogs = Arrays.stream(SleepQuality.values())
                .filter(sleepQuality -> !sleepQualityAggregation.containsKey(sleepQuality))
                .collect(Collectors.toList());

        qualitiesNotPresentInLogs.forEach(q -> sleepQualityAggregation.put(q, 0));

        return sleepQualityAggregation;
    }
}
