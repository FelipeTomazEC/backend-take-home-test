package com.noom.interview.fullstack.sleep.domain

import com.noom.interview.fullstack.sleep.domain.errors.InvalidBedTimeIntervalException
import spock.lang.Specification

import java.time.LocalDateTime

class SleepLogTest extends Specification {

    def "Bed time is always before wake up time"() {
        given: "A start time that is after the end time"
        def bedTime = LocalDateTime.now()
        def wakeUpTime = bedTime.minusSeconds(1)

        when: "Creating a sleep log"
        new SleepLog(bedTime, wakeUpTime, SleepQuality.BAD)

        then: "An exception is thrown"
        thrown(InvalidBedTimeIntervalException)
    }

    def "Total time in bed is calculated based on time in bed interval"() {
        given: "A sleep log with a time in bed interval"
        def bedTime = LocalDateTime.of(2025, 01, 1, 22, 53)
        def wakeUpTime = LocalDateTime.of(2025, 01, 2, 7, 5)
        def sleepLog = new SleepLog(bedTime, wakeUpTime, SleepQuality.GOOD)

        when:
        def totalTImeInBed = sleepLog.getTotalSleepTimeInBed()

        then: "The total time in bed is calculated correctly"
        totalTImeInBed.hour== 8
        totalTImeInBed.minute == 12
    }

    def "Sleep date is derived from wake up time"() {
        given: "A sleep log for last night"
        def bedTime = LocalDateTime.of(2025, 05, 1, 23, 30)
        def wakeUpTime = bedTime.plusHours(8)
        def sleepLog = new SleepLog(bedTime, wakeUpTime, SleepQuality.GOOD)

        when: "retrieving the sleep date"
        def sleepDate = sleepLog.getSleepDate()

        then: "The sleep date is the same as the wake up time date"
        sleepDate.dayOfMonth == wakeUpTime.dayOfMonth
        sleepDate.month == wakeUpTime.month
    }

    def "A sleep log cannot be created in a future date"() {
        given: "A sleep log with a bed time in the future"
        def bedTime = LocalDateTime.now()
        def wakeUpTime = bedTime.plusHours(1)

        when: "Creating a sleep log"
        new SleepLog(bedTime, wakeUpTime, SleepQuality.GOOD)

        then: "An exception is thrown"
        thrown(InvalidBedTimeIntervalException)
    }
}
