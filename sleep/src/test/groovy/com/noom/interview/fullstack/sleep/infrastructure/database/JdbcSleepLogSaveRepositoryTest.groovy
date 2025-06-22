package com.noom.interview.fullstack.sleep.infrastructure.database

import com.noom.interview.fullstack.sleep.application.ports.repositories.SaveSleepLogRepository
import com.noom.interview.fullstack.sleep.domain.SleepLog
import com.noom.interview.fullstack.sleep.domain.SleepQuality
import com.noom.interview.fullstack.sleep.testutils.AbstractDatabaseTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

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

    def retrieveSleepLogByUserId(UUID userId) {
        def sql = """
                    SELECT sleep_date, bed_time, wake_up_time, quality
                    FROM sleep_logs 
                    WHERE user_id = :userId
        """

        def params = ["userId": userId]

        return jdbcTemplate.queryForObject(sql, params, (rs, rn) -> {
            var wakeUpTime = rs.getObject("wake_up_time", LocalTime.class)
            var bedTime = rs.getObject("bed_time", LocalTime.class)
            var sleepDate = rs.getObject("sleep_date", LocalDate.class)
            var quality = SleepQuality.valueOf(rs.getString("quality"))

            return SleepLog.builder()
                    .bedTime(LocalDateTime.of(sleepDate.minusDays(1), bedTime))
                    .wakeUpTime(LocalDateTime.of(sleepDate, wakeUpTime))
                    .quality(quality)
                    .build()
        })
    }
}
