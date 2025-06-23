package com.noom.interview.fullstack.sleep.application.usecases

import com.noom.interview.fullstack.sleep.application.ports.commands.GetSleepSummaryCommand
import com.noom.interview.fullstack.sleep.application.ports.repositories.GetLogsFromPeriodRepository
import com.noom.interview.fullstack.sleep.application.services.AverageTimeCalculator
import com.noom.interview.fullstack.sleep.domain.SleepQuality
import com.noom.interview.fullstack.sleep.testutils.TestEntitiesBuilder
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalTime

class GetSleepSummaryUseCaseTest extends Specification {

    def repository = Mock(GetLogsFromPeriodRepository)
    def averageTimeCalculator = Mock(AverageTimeCalculator)

    @Subject
    def useCase = new GetSleepSummaryUseCase(repository, averageTimeCalculator)

    def "Retrieves sleep summary for a given period"() {
        given: "a command to get sleep summary for a specific period"
        def startDate = LocalDate.now().minusDays(7)
        def endDate = LocalDate.now()
        def userId = UUID.randomUUID()
        def command = GetSleepSummaryCommand.builder()
                .startDate(startDate)
                .endDate(endDate)
                .userId(userId)
                .build()

        and: "the user has sleep logs for that period"
        def sleepLogs = [
                TestEntitiesBuilder.buildSleepLog().quality(SleepQuality.GOOD).build(),
                TestEntitiesBuilder.buildSleepLog().quality(SleepQuality.OK).build(),
                TestEntitiesBuilder.buildSleepLog().quality(SleepQuality.BAD).build(),
                TestEntitiesBuilder.buildSleepLog().quality(SleepQuality.BAD).build()
        ]
        repository.findByPeriod(startDate, endDate, userId) >> sleepLogs

        and: "the average time of sleep  is calculated"
        def sleepTimes = sleepLogs.collect { it.getTotalSleepTimeInBed() }
        def averageSleepTime = LocalTime.of(8, 30)
        1 * averageTimeCalculator.calculateAvgTime(sleepTimes) >> averageSleepTime

        and: "the average wake up time is calculated"
        def wakeUpTimes = sleepLogs.collect { it.wakeUpTime }
        def averageWakeUpTime = LocalTime.of(5, 30)
        1 * averageTimeCalculator.calculateAvgTime(wakeUpTimes) >> averageWakeUpTime

        and: "the average bed time is calculated"
        def bedTimes = sleepLogs.collect { it.bedTime }
        def averageBedTime = LocalTime.of(21, 30)
        1 * averageTimeCalculator.calculateAvgTime(bedTimes) >> averageBedTime

        when:
        def result = useCase.execute(command)

        then: "the frequency of each sleep quality is calculated correctly"
        result.sleepQualityFrequency.get(SleepQuality.GOOD) == 1
        result.sleepQualityFrequency.get(SleepQuality.OK) == 1
        result.sleepQualityFrequency.get(SleepQuality.BAD) == 2

        and: "the average times are correctly set in the result"
        result.averageBedTime == averageBedTime
        result.averageWakeUpTime == averageWakeUpTime
        result.averageSleepTime == averageSleepTime
    }

    def "Quality frequency shows zero for quality not present in logs"() {
        given: "a command to get sleep summary for a specific period"
        def startDate = LocalDate.now().minusDays(7)
        def endDate = LocalDate.now()
        def userId = UUID.randomUUID()
        def command = GetSleepSummaryCommand.builder()
                .startDate(startDate)
                .endDate(endDate)
                .userId(userId)
                .build()

        and: "the user has sleep logs for that period"
        def sleepLogs = [
                TestEntitiesBuilder.buildSleepLog().quality(SleepQuality.GOOD).build(),
                TestEntitiesBuilder.buildSleepLog().quality(SleepQuality.OK).build()
        ]
        repository.findByPeriod(startDate, endDate, userId) >> sleepLogs

        when:
        def result = useCase.execute(command)

        then: "the frequency of BAD quality is zero"
        result.sleepQualityFrequency.get(SleepQuality.BAD) == 0
    }
}
