package com.noom.interview.fullstack.sleep.application.usecases;

import com.noom.interview.fullstack.sleep.application.ports.operations.GetSleepLogFromSpecificDateOperation;
import com.noom.interview.fullstack.sleep.application.ports.output.GetSleepLogFromSpecificDateOutput;
import com.noom.interview.fullstack.sleep.application.ports.repositories.GetSleepLogFromDateRepository;
import com.noom.interview.fullstack.sleep.domain.errors.NoLogsForThisDateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class GetSleepLogFromSpecificDateUseCase implements
        UseCase<GetSleepLogFromSpecificDateOperation, GetSleepLogFromSpecificDateOutput> {

    private final GetSleepLogFromDateRepository getSleepLogFromDateRepository;

    @Override
    public GetSleepLogFromSpecificDateOutput execute(GetSleepLogFromSpecificDateOperation operation) {
        log.info("Retrieving sleep log for a specific date: userId={}, date={}", operation.getUserId(), operation.getDate());

        var possibleSleepLog = getSleepLogFromDateRepository.findByDate(
                operation.getDate(),
                operation.getUserId()
        );

        return possibleSleepLog.map(sleepLog -> GetSleepLogFromSpecificDateOutput.builder()
                        .date(sleepLog.getSleepDate())
                        .totalSleepTime(sleepLog.getTotalSleepTimeInBed())
                        .bedTime(sleepLog.getBedTime())
                        .wakeUpTime(sleepLog.getWakeUpTime())
                        .sleepQuality(sleepLog.getQuality())
                        .build()
                )
                .orElseThrow(() -> new NoLogsForThisDateException(operation.getDate()));
    }
}
