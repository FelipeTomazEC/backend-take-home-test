package com.noom.interview.fullstack.sleep.testutils

import com.noom.interview.fullstack.sleep.domain.SleepLog
import com.noom.interview.fullstack.sleep.domain.SleepQuality
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


@SpringBootTest
abstract class AbstractDatabaseTest extends Specification {
    static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("sleep_test_db")
            .withUsername("sleep_test_user")
            .withPassword("sleep_test_password")

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate

    def setupSpec() {
        POSTGRES.start()
    }

    def cleanupSpec() {
        POSTGRES.stop()
    }

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl)
        registry.add("spring.datasource.username", POSTGRES::getUsername)
        registry.add("spring.datasource.password", POSTGRES::getPassword)
    }

    SleepLog retrieveSleepLogByUserId(UUID userId) {
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
