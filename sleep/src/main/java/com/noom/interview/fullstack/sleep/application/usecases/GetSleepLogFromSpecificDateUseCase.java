package com.noom.interview.fullstack.sleep.application.usecases;

import com.noom.interview.fullstack.sleep.application.ports.commands.GetSleepLogFromSpecificDateCommand;
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
        UseCase<GetSleepLogFromSpecificDateCommand, GetSleepLogFromSpecificDateOutput> {

    private final GetSleepLogFromDateRepository getSleepLogFromDateRepository;

    @Override
    public GetSleepLogFromSpecificDateOutput execute(GetSleepLogFromSpecificDateCommand command) {
        log.info("Retrieving sleep log for a specific date: userId={}, date={}", command.getUserId(), command.getDate());

        var possibleSleepLog = getSleepLogFromDateRepository.findByDate(
                command.getDate(),
                command.getUserId()
        );

        return possibleSleepLog.map(sleepLog -> GetSleepLogFromSpecificDateOutput.builder()
                        .date(sleepLog.getSleepDate())
                        .totalSleepTime(sleepLog.getTotalSleepTimeInBed())
                        .bedTime(sleepLog.getBedTime())
                        .wakeUpTime(sleepLog.getWakeUpTime())
                        .sleepQuality(sleepLog.getQuality().name())
                        .build()
                )
                .orElseThrow(() -> new NoLogsForThisDateException(command.getDate()));
    }
}
