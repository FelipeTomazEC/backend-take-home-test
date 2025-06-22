package com.noom.interview.fullstack.sleep.testutils

import com.noom.interview.fullstack.sleep.domain.SleepLog
import com.noom.interview.fullstack.sleep.domain.SleepQuality

import java.time.LocalDate

class TestEntitiesBuilder {
    static SleepLog.SleepLogBuilder buildSleepLog() {
        def yesterdayNight = LocalDate.now().minusDays(1).atTime(22, 0)
        def todayMorning = LocalDate.now().atTime(6, 0)

        return SleepLog.builder()
                .quality(SleepQuality.GOOD)
                .bedTime(yesterdayNight)
                .wakeUpTime(todayMorning)
    }
}
