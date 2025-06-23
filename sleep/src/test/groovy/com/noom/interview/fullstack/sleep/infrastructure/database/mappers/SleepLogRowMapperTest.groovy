package com.noom.interview.fullstack.sleep.infrastructure.database.mappers

import com.noom.interview.fullstack.sleep.domain.SleepLog
import com.noom.interview.fullstack.sleep.domain.SleepQuality
import spock.lang.Specification
import spock.lang.Subject

import java.sql.ResultSet
import java.time.LocalDate
import java.time.LocalTime

class SleepLogRowMapperTest extends Specification {

    @Subject
    def sleepLogRowMapper = new SleepLogRowMapper()

    def "Map result set correctly - Bed time is from day before"() {
        given:
        def rs = Mock(ResultSet)
        def dbBedTime = LocalTime.of(22, 0)
        def dbWakeUpTime = LocalTime.of(6, 0)
        def dbSleepDate = LocalDate.of(2024, 6, 10)
        def dbQuality = SleepQuality.GOOD
        rs.getObject("bed_time", LocalTime) >> dbBedTime
        rs.getObject("wake_up_time", LocalTime) >> dbWakeUpTime
        rs.getObject("sleep_date", LocalDate) >> dbSleepDate
        rs.getString("quality") >> dbQuality.name()

        when:
        SleepLog log = sleepLogRowMapper.mapRow(rs, 1)

        then:
        log.sleepDate == dbSleepDate
        log.bedTime == dbBedTime
        log.wakeUpTime == dbWakeUpTime
        log.quality == SleepQuality.GOOD
        log.totalSleepTimeInBed == LocalTime.of(8, 0)
    }

    def "Map result set correctly - Bed time is in the same date as wake up time"() {
        given:
        def rs = Mock(ResultSet)
        def dbBedTime = LocalTime.of(12, 0)
        def dbWakeUpTime = LocalTime.of(20, 0)
        def dbSleepDate = LocalDate.of(2024, 6, 10)
        def dbQuality = SleepQuality.GOOD
        rs.getObject("bed_time", LocalTime) >> dbBedTime
        rs.getObject("wake_up_time", LocalTime) >> dbWakeUpTime
        rs.getObject("sleep_date", LocalDate) >> dbSleepDate
        rs.getString("quality") >> dbQuality.name()

        when:
        SleepLog log = sleepLogRowMapper.mapRow(rs, 1)

        then:
        log.sleepDate == dbSleepDate
        log.bedTime == dbBedTime
        log.wakeUpTime == dbWakeUpTime
        log.quality == SleepQuality.GOOD
        log.totalSleepTimeInBed == LocalTime.of(8, 0)
    }
}
