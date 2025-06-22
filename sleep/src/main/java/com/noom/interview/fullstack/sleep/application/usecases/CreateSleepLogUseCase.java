package com.noom.interview.fullstack.sleep.application.usecases;

import com.noom.interview.fullstack.sleep.application.ports.commands.CreateSleepLogCommand;
import com.noom.interview.fullstack.sleep.application.ports.repositories.SaveSleepLogRepository;
import com.noom.interview.fullstack.sleep.domain.SleepLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateSleepLogUseCase implements UseCase<CreateSleepLogCommand, Void> {

    private final SaveSleepLogRepository saveSleepLogRepository;

    @Override
    public Void execute(CreateSleepLogCommand command) {
        log.info("Creating new sleep log entry: userId={}, date={}", command.getUserId(),  command.getWakeUpTime());

        var sleepLog = SleepLog.builder()
                .bedTime(command.getBedTime())
                .wakeUpTime(command.getWakeUpTime())
                .quality(command.getQuality())
                .build();

        saveSleepLogRepository.save(sleepLog, command.getUserId());

        log.info("Sleep log entry created successfully: userId: {}, date={}", command.getUserId(), command.getWakeUpTime());

        return null;
    }
}
