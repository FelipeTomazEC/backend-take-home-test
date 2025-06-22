package com.noom.interview.fullstack.sleep.application.usecases

import com.noom.interview.fullstack.sleep.application.ports.commands.CreateSleepLogCommand
import com.noom.interview.fullstack.sleep.application.ports.repositories.SaveSleepLogRepository
import com.noom.interview.fullstack.sleep.domain.SleepLog
import com.noom.interview.fullstack.sleep.domain.SleepQuality
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class CreateSleepLogUseCaseTest extends Specification {

    def sleepLogRepository = Mock(SaveSleepLogRepository)

    @Subject
    def createSleepLogUseCase = new CreateSleepLogUseCase(sleepLogRepository)

    def "Create a sleep log and stores it in a repository"() {
        given: "A create sleep log command with valid parameters"
        def userId = UUID.randomUUID()
        def command = CreateSleepLogCommand.builder()
                .bedTime(LocalDateTime.now().minusHours(5))
                .wakeUpTime(LocalDateTime.now())
                .quality(SleepQuality.OK)
                .userId(userId)
                .build()

        when: "The use case is executed"
        createSleepLogUseCase.execute(command)

        then: "The repository is used to store the sleep log"
        1 * sleepLogRepository.save({ SleepLog log ->
            log.quality == command.quality
            log.bedTime == command.bedTime.toLocalTime()
            log.wakeUpTime == command.wakeUpTime.toLocalTime()
        }, userId)
    }
}
