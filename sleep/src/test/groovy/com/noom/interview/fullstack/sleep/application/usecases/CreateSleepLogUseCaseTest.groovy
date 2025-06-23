package com.noom.interview.fullstack.sleep.application.usecases

import com.noom.interview.fullstack.sleep.application.ports.commands.CreateSleepLogCommand
import com.noom.interview.fullstack.sleep.application.ports.repositories.GetSleepLogFromDateRepository
import com.noom.interview.fullstack.sleep.application.ports.repositories.SaveSleepLogRepository
import com.noom.interview.fullstack.sleep.domain.SleepLog
import com.noom.interview.fullstack.sleep.domain.SleepQuality
import com.noom.interview.fullstack.sleep.domain.errors.SleepLogAlreadyExistsException
import com.noom.interview.fullstack.sleep.testutils.TestEntitiesBuilder
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalDateTime

class CreateSleepLogUseCaseTest extends Specification {

    def sleepLogRepository = Mock(SaveSleepLogRepository)
    def getLogFromSpecificDateRepository = Mock(GetSleepLogFromDateRepository)

    @Subject
    def createSleepLogUseCase = new CreateSleepLogUseCase(
            sleepLogRepository,
            getLogFromSpecificDateRepository
    )

    def "Create a sleep log and stores it in a repository"() {
        given: "A create sleep log command with valid parameters"
        def userId = UUID.randomUUID()
        def command = CreateSleepLogCommand.builder()
                .bedTime(LocalDateTime.now().minusHours(5))
                .wakeUpTime(LocalDateTime.now())
                .quality(SleepQuality.OK)
                .userId(userId)
                .build()

        and: "The user does not have a log for today"
        getLogFromSpecificDateRepository.findByDate(command.wakeUpTime.toLocalDate(), userId) >> Optional.empty()

        when: "The use case is executed"
        createSleepLogUseCase.execute(command)

        then: "The repository is used to store the sleep log"
        1 * sleepLogRepository.save({ SleepLog log ->
            log.quality == command.quality
            log.bedTime == command.bedTime.toLocalTime()
            log.wakeUpTime == command.wakeUpTime.toLocalTime()
        }, userId)
    }

    def "User cannot have more than one log per day"() {
        given: "A user has an existing sleep log for today"
        def userId = UUID.randomUUID()
        getLogFromSpecificDateRepository.findByDate(LocalDate.now(), userId) >>
                Optional.of(TestEntitiesBuilder.buildSleepLog().build())

        and: "we try to create a new sleep log for today"
        def command = CreateSleepLogCommand.builder()
                .bedTime(LocalDateTime.now().minusHours(5))
                .wakeUpTime(LocalDateTime.now())
                .quality(SleepQuality.OK)
                .userId(userId)
                .build()

        when: "The use case is executed"
        createSleepLogUseCase.execute(command)

        then: "An exception is thrown indicating the user already has a log for today"
        thrown(SleepLogAlreadyExistsException)
    }
}
