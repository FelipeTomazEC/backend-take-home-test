package com.noom.interview.fullstack.sleep.application.usecases

import com.noom.interview.fullstack.sleep.application.ports.operations.GetSleepLogFromSpecificDateOperation
import com.noom.interview.fullstack.sleep.application.ports.repositories.GetSleepLogFromDateRepository
import com.noom.interview.fullstack.sleep.domain.SleepLog
import com.noom.interview.fullstack.sleep.domain.SleepQuality
import com.noom.interview.fullstack.sleep.domain.errors.NoLogsForThisDateException
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalDateTime

class GetSleepLogFromSpecificDateUseCaseTest extends Specification {

    def repository = Mock(GetSleepLogFromDateRepository)

    @Subject
    def useCase = new GetSleepLogFromSpecificDateUseCase(repository)

    def "No logs found for specific date"() {
        given: "operation to get sleep log for a specific date"
        def operation = GetSleepLogFromSpecificDateOperation.builder()
                .date(LocalDate.now())
                .userId(UUID.randomUUID())
                .build()

        and: "there are no logs in the repository for that date related to that user"
        repository.findByDate(operation.date, operation.userId) >> Optional.empty()

        when:
        useCase.execute(operation)

        then:
        thrown(NoLogsForThisDateException)
    }

    def "Logs found for specific date"() {
        given: "operation to get sleep log for a specific date"
        def operation = GetSleepLogFromSpecificDateOperation.builder()
                .date(LocalDate.now())
                .userId(UUID.randomUUID())
                .build()

        and: "there are logs in the repository for that date related to that user"
        def sleepTimeInHours = 8
        def sleepLog = SleepLog.builder()
                .quality(SleepQuality.GOOD)
                .bedTime(LocalDateTime.now().minusHours(sleepTimeInHours))
                .wakeUpTime(LocalDateTime.now())
                .build()

        repository.findByDate(operation.date, operation.userId) >> Optional.of(sleepLog)

        when:
        def result = useCase.execute(operation)

        then:
        result.bedTime == sleepLog.bedTime
        result.wakeUpTime == sleepLog.wakeUpTime
        result.sleepQuality == sleepLog.quality
        result.totalSleepTime == sleepLog.totalSleepTimeInBed
        result.date == sleepLog.sleepDate
    }
}
