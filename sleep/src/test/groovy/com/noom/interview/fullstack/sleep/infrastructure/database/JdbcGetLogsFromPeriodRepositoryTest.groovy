package com.noom.interview.fullstack.sleep.infrastructure.database

import com.noom.interview.fullstack.sleep.application.ports.repositories.GetLogsFromPeriodRepository
import com.noom.interview.fullstack.sleep.application.ports.repositories.SaveSleepLogRepository
import com.noom.interview.fullstack.sleep.testutils.AbstractDatabaseTest
import com.noom.interview.fullstack.sleep.testutils.TestEntitiesBuilder
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Subject

import java.time.LocalDate

class JdbcGetLogsFromPeriodRepositoryTest extends AbstractDatabaseTest {

    @Autowired
    SaveSleepLogRepository saveSleepLogRepository

    @Subject
    @Autowired
    GetLogsFromPeriodRepository getLogsFromPeriodRepository

    def "Retrieve logs from a user for a given period"() {
        given: "a user has multiple sleep logs for a specific period"
        def userId = UUID.randomUUID()
        def startTime = LocalDate.now().minusDays(7)
        def endTime = LocalDate.now()
        def sleepLogOne = TestEntitiesBuilder.buildSleepLog()
                .bedTime(startTime.atTime(22, 0))
                .wakeUpTime(startTime.plusDays(1).atTime(6, 0))
                .build()
        def sleepLogTwo = TestEntitiesBuilder.buildSleepLog()
                .bedTime(startTime.plusDays(1).atTime(22, 0))
                .wakeUpTime(startTime.plusDays(2).atTime(6, 0))
                .build()
        def sleepLogThree = TestEntitiesBuilder.buildSleepLog()
                .bedTime(endTime.minusDays(1).atTime(22, 0))
                .wakeUpTime(endTime.atTime(6, 0))
                .build()

        saveSleepLogRepository.save(sleepLogOne, userId)
        saveSleepLogRepository.save(sleepLogTwo, userId)
        saveSleepLogRepository.save(sleepLogThree, userId)

        when: "we retrieve logs for the specified period"
        def logs = getLogsFromPeriodRepository.findByPeriod(startTime, endTime, userId)

        then: "the logs are correctly retrieved"
        logs.size() == 3
    }

    def "No sleep logs found for a given period"() {
        given: "a user specifies a period"
        def userId = UUID.randomUUID()
        def startTime = LocalDate.now().minusDays(7)
        def endTime = LocalDate.now()

        and: "their sleep logs are not for that period"
        def sleepLogOne = TestEntitiesBuilder.buildSleepLog()
                .bedTime(startTime.minusDays(2).atTime(22, 0))
                .wakeUpTime(startTime.minusDays(1).atTime(6, 0))
                .build()

        def sleepLogTwo = TestEntitiesBuilder.buildSleepLog()
                .bedTime(endTime.atTime(22, 0))
                .wakeUpTime(endTime.plusDays(1).atTime(6, 0))
                .build()

        saveSleepLogRepository.save(sleepLogOne, userId)
        saveSleepLogRepository.save(sleepLogTwo, userId)

        when: "we try to retrieve logs for the specified period"
        def logs = getLogsFromPeriodRepository.findByPeriod(startTime, endTime, userId)

        then: "no logs are found"
        logs.isEmpty()
    }
}
