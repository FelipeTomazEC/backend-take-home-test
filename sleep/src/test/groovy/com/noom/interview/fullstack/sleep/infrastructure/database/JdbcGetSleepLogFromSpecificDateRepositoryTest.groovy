package com.noom.interview.fullstack.sleep.infrastructure.database

import com.noom.interview.fullstack.sleep.application.ports.repositories.GetSleepLogFromDateRepository
import com.noom.interview.fullstack.sleep.application.ports.repositories.SaveSleepLogRepository
import com.noom.interview.fullstack.sleep.domain.SleepLog
import com.noom.interview.fullstack.sleep.domain.SleepQuality
import com.noom.interview.fullstack.sleep.testutils.AbstractDatabaseTest
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalDateTime

class JdbcGetSleepLogFromSpecificDateRepositoryTest extends AbstractDatabaseTest {

    @Autowired
    SaveSleepLogRepository saveSleepLogRepository

    @Subject
    @Autowired
    GetSleepLogFromDateRepository repository

    def "Logs can be retrieved based on user and date"() {
        given: "a user has a sleep log entry for two days ago"
        def userId = UUID.randomUUID()
        def sleepLogForTwoDaysAgo = SleepLog.builder()
                .bedTime(LocalDateTime.now().minusDays(2).minusHours(8))
                .wakeUpTime(LocalDateTime.now().minusDays(2))
                .quality(SleepQuality.GOOD)
                .build()
        saveSleepLogRepository.save(sleepLogForTwoDaysAgo, userId)

        when: "we try to get the sleep log for two days ago"
        def twoDaysAgo = LocalDate.now().minusDays(2)
        def logFromTwoDaysAgo = repository.findByDate(twoDaysAgo, userId)

        then: "the log is found and matches the expected values"
        logFromTwoDaysAgo.isPresent()

        and: "the log is correctly populated"
        def retrievedLog = logFromTwoDaysAgo.get()
        retrievedLog.bedTime == sleepLogForTwoDaysAgo.bedTime
        retrievedLog.wakeUpTime == sleepLogForTwoDaysAgo.wakeUpTime
        retrievedLog.quality == sleepLogForTwoDaysAgo.quality

        when: "we try to get the sleep log for yesterday"
        def yesterday = LocalDate.now().minusDays(1)
        def result = repository.findByDate(yesterday, userId)

        then: "no logs are found for that date"
        result.isEmpty()
    }
}
