package com.noom.interview.fullstack.sleep.infrastructure.controllers

import com.noom.interview.fullstack.sleep.api.commons.Constants
import com.noom.interview.fullstack.sleep.api.v1.SleepLogsApiV1
import com.noom.interview.fullstack.sleep.api.v1.responses.GetSleepSummaryHttpResponse
import com.noom.interview.fullstack.sleep.application.ports.repositories.SaveSleepLogRepository
import com.noom.interview.fullstack.sleep.domain.SleepQuality
import com.noom.interview.fullstack.sleep.testutils.AbstractControllerTest
import com.noom.interview.fullstack.sleep.testutils.TestEntitiesBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

import java.time.LocalDate
import java.time.LocalTime

class GetSleepSummaryControllerTest extends AbstractControllerTest {

    @Autowired
    SaveSleepLogRepository saveSleepLogRepository

    def "Field #fieldName is not in correct format"() {
        when: "A user requests a sleep log with an invalid date format"
        def response = createHttpRequest()
                .with().header(Constants.USER_ID_HEADER, UUID.randomUUID().toString())
                .and().queryParam("startDate", startDate)
                .and().queryParam("endDate", endDate)
                .when().get(SleepLogsApiV1.GET_SLEEP_SUMMARY)

        then: "The response status is BAD_REQUEST"
        response.statusCode() == HttpStatus.BAD_REQUEST.value()

        and: "response contains a meaningful error message"
        def responseBody = response.body().asString()
        responseBody.contains(fieldName)
        responseBody.contains("Invalid")
        responseBody.contains("yyyy-MM-dd")

        where:
        fieldName   | startDate        | endDate
        "startDate" | "202-01-01"    | "2023-01-02"
        "endDate"   | "2023-01-01" | "202-01-02"
    }

    def "Retrieve sleep summary for the given period"() {
        given: "user has some sleep logs in the database"
        def userId = UUID.randomUUID()
        def startTime = LocalDate.now().minusDays(7)
        def endTime = LocalDate.now()
        def sleepLogOne = TestEntitiesBuilder.buildSleepLog()
                .quality(SleepQuality.GOOD)
                .bedTime(startTime.atTime(22, 0))
                .wakeUpTime(startTime.plusDays(1).atTime(6, 0))
                .build()
        def sleepLogTwo = TestEntitiesBuilder.buildSleepLog()
                .bedTime(endTime.minusDays(1).atTime(22, 0))
                .quality(SleepQuality.GOOD)
                .wakeUpTime(endTime.atTime(6, 0))
                .build()

        saveSleepLogRepository.save(sleepLogOne, userId)
        saveSleepLogRepository.save(sleepLogTwo, userId)

        when: "requesting sleep summary for the period"
        def response = createHttpRequest()
                .with().header(Constants.USER_ID_HEADER, userId.toString())
                .and().queryParam("startDate", startTime.toString())
                .and().queryParam("endDate", endTime.toString())
                .when().get(SleepLogsApiV1.GET_SLEEP_SUMMARY)

        then: "the response status is OK"
        response.statusCode() == HttpStatus.OK.value()

        and: "the response body contains the correct summary data"
        def responseBody = response.body().as(GetSleepSummaryHttpResponse)
        responseBody.averageSleepTime == LocalTime.of(8, 0).toString()
        responseBody.averageWakeUpTime == LocalTime.of(6, 0).toString()
        responseBody.averageBedTime == LocalTime.of(22, 0).toString()
        responseBody.period.startDate == startTime.toString()
        responseBody.period.endDate == endTime.toString()
        responseBody.sleepQualityFrequency == [
                (SleepQuality.GOOD.name()): 2,
                (SleepQuality.OK.name()): 0,
                (SleepQuality.BAD.name()): 0,
        ]
    }

    def "Defaults to last 30 days"() {
        given: "user has some sleep logs in the database"
        def userId = UUID.randomUUID()
        def startTime = LocalDate.now().minusDays(30)
        def endTime = LocalDate.now()
        def sleepLogOne = TestEntitiesBuilder.buildSleepLog()
                .quality(SleepQuality.GOOD)
                .bedTime(startTime.minusDays(1).atTime(22, 0))
                .wakeUpTime(startTime.atTime(6, 0))
                .build()
        def sleepLogTwo = TestEntitiesBuilder.buildSleepLog()
                .bedTime(endTime.minusDays(1).atTime(22, 0))
                .quality(SleepQuality.GOOD)
                .wakeUpTime(endTime.atTime(6, 0))
                .build()

        saveSleepLogRepository.save(sleepLogOne, userId)
        saveSleepLogRepository.save(sleepLogTwo, userId)

        when: "requesting sleep summary without specifying dates"
        def response = createHttpRequest()
                .with().header(Constants.USER_ID_HEADER, userId.toString())
                .when().get(SleepLogsApiV1.GET_SLEEP_SUMMARY)

        then: "the response status is OK"
        response.statusCode() == HttpStatus.OK.value()

        and: "the response body contains the correct summary data"
        def responseBody = response.body().as(GetSleepSummaryHttpResponse)
        responseBody.averageSleepTime == LocalTime.of(8, 0).toString()
        responseBody.averageWakeUpTime == LocalTime.of(6, 0).toString()
        responseBody.averageBedTime == LocalTime.of(22, 0).toString()
        responseBody.period.startDate == startTime.toString()
        responseBody.period.endDate == endTime.toString()
        responseBody.sleepQualityFrequency == [
                (SleepQuality.GOOD.name()): 2,
                (SleepQuality.OK.name()): 0,
                (SleepQuality.BAD.name()): 0,
        ]
    }
}
