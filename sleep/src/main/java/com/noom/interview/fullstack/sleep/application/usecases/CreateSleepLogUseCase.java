package com.noom.interview.fullstack.sleep.application.usecases;

import com.noom.interview.fullstack.sleep.application.ports.operations.CreateSleepLogOperation;
import com.noom.interview.fullstack.sleep.application.ports.repositories.GetSleepLogFromDateRepository;
import com.noom.interview.fullstack.sleep.application.ports.repositories.SaveSleepLogRepository;
import com.noom.interview.fullstack.sleep.domain.SleepLog;
import com.noom.interview.fullstack.sleep.domain.errors.SleepLogAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateSleepLogUseCase implements UseCase<CreateSleepLogOperation, Void> {

    private final SaveSleepLogRepository saveSleepLogRepository;
    private final GetSleepLogFromDateRepository getSleepLogFromDateRepository;

    @Override
    public Void execute(CreateSleepLogOperation operation) {
        log.info("Creating new sleep log entry: userId={}, date={}", operation.getUserId(),  operation.getWakeUpTime());

        var isSleepLogAlreadyRegistered = getSleepLogFromDateRepository.findByDate(
                operation.getWakeUpTime().toLocalDate(),
                operation.getUserId()
        ).isPresent();

        if (isSleepLogAlreadyRegistered) {
            log.info("Sleep log already exists for date: userId={}, date={}", operation.getUserId(), operation.getWakeUpTime());
            throw new SleepLogAlreadyExistsException(operation.getWakeUpTime().toLocalDate());
        }

        var sleepLog = SleepLog.builder()
                .bedTime(operation.getBedTime())
                .wakeUpTime(operation.getWakeUpTime())
                .quality(operation.getQuality())
                .build();

        saveSleepLogRepository.save(sleepLog, operation.getUserId());

        log.info("Sleep log entry created successfully: userId: {}, date={}", operation.getUserId(), operation.getWakeUpTime());

        return null;
    }
}
