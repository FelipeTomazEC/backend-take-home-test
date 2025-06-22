package com.noom.interview.fullstack.sleep.infrastructure.database

import com.noom.interview.fullstack.sleep.application.ports.repositories.SaveSleepLogRepository
import com.noom.interview.fullstack.sleep.domain.SleepLog
import com.noom.interview.fullstack.sleep.domain.SleepQuality
import com.noom.interview.fullstack.sleep.testutils.AbstractDatabaseTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import spock.lang.Subject

import java.time.LocalDateTime

class JdbcSleepLogSaveRepositoryTest extends AbstractDatabaseTest {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate

    @Subject
    @Autowired
     SaveSleepLogRepository saveSleepLogRepository

    def "Saves a sleep log in the database"() {
        given: "A sleep log instance"
        def userId = UUID.randomUUID()
        def sleepLog = SleepLog.builder()
                .bedTime(LocalDateTime.now().minusHours(5))
                .wakeUpTime(LocalDateTime.now())
                .quality(SleepQuality.OK)
                .build()

        when:
        saveSleepLogRepository.save(sleepLog, userId)

        then: "The sleep log is saved successfully"
        def retrievedLog = retrieveSleepLogByUserId(userId)
        retrievedLog.wakeUpTime == sleepLog.wakeUpTime
        retrievedLog.bedTime == sleepLog.bedTime
        retrievedLog.quality == sleepLog.quality
        retrievedLog.sleepDate == sleepLog.sleepDate
    }
}
