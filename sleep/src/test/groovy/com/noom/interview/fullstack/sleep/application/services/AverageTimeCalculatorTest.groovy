package com.noom.interview.fullstack.sleep.application.services

import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalTime

class AverageTimeCalculatorTest extends Specification {

    @Subject
    def averageTimeCalculator = new AverageTimeCalculator()

    def "Calculates the average of a list of times"() {
        when: "a list of times is provided"
        def average = averageTimeCalculator.calculateAvgTime(times)

        then: "the average time is calculated correctly"
        average.hour == expected.hour
        average.minute == expected.minute

        where:
        expected                     | times
        LocalTime.of(10, 0)   | [LocalTime.of(10, 0)]
        LocalTime.of(10, 0)   | [LocalTime.of(10, 0), LocalTime.of(10, 0), LocalTime.of(10, 0)]
        LocalTime.of(5, 0)     | [LocalTime.of(10, 0), LocalTime.of(0, 0)]
        LocalTime.of(22, 30) | [LocalTime.of(23, 0), LocalTime.of(22, 0), LocalTime.of(22, 30)]
        LocalTime.of(7, 15)   | [LocalTime.of(8, 0), LocalTime.of(6, 30), LocalTime.of(7, 15)]
        LocalTime.of(0, 0)     | []
    }
}
