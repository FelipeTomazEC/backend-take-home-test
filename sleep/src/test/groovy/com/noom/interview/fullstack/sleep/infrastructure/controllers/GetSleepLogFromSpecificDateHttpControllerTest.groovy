package com.noom.interview.fullstack.sleep.infrastructure.controllers

import com.noom.interview.fullstack.sleep.api.commons.Constants
import com.noom.interview.fullstack.sleep.api.v1.SleepLogsApiV1
import com.noom.interview.fullstack.sleep.api.v1.responses.GetSleepLogFromSpecificDateHttpResponse
import com.noom.interview.fullstack.sleep.application.ports.repositories.SaveSleepLogRepository
import com.noom.interview.fullstack.sleep.testutils.AbstractControllerTest
import com.noom.interview.fullstack.sleep.testutils.TestEntitiesBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

import java.time.LocalDate

class GetSleepLogFromSpecificDateHttpControllerTest extends AbstractControllerTest {

    @Autowired
    SaveSleepLogRepository saveSleepLogRepository

    def "Sleep log is not found"() {
        when: "A user requests a sleep log for a specific date with no logs available"
        def date = LocalDate.now()
        def response = createHttpRequest()
                .with().header(Constants.USER_ID_HEADER, UUID.randomUUID().toString())
                .and().queryParam("date", date.toString())
                .when().get(SleepLogsApiV1.GET_SLEEP_LOG_FROM_SPECIFIC_DATE)

        then: "The response contains the correct sleep log data"
        response.statusCode() == HttpStatus.NOT_FOUND.value()

        and: "response contains a meaningful error message"
        def responseBody = response.body().asString()
        responseBody.contains("No sleep logs found")
        responseBody.contains(date.toString())
    }

    def "Sleep log is found"() {
        given: "A user has a sleep log for a specific date"
        def userId = UUID.randomUUID()
        def sleepLog = TestEntitiesBuilder.buildSleepLog().build()
        saveSleepLogRepository.save(sleepLog, userId)

        when: "A user requests a sleep log for that specific date"
        def response = createHttpRequest()
                .with().header(Constants.USER_ID_HEADER, userId.toString())
                .and().queryParam("date", sleepLog.getSleepDate().toString())
                .when().get(SleepLogsApiV1.GET_SLEEP_LOG_FROM_SPECIFIC_DATE)

        then: "The response status is OK"
        response.statusCode() == HttpStatus.OK.value()

        and: "sleep log data is returned in the response body"
        def responseBody = response.body().as(GetSleepLogFromSpecificDateHttpResponse)
        responseBody.wakeUpTime == sleepLog.wakeUpTime.toString()
        responseBody.bedTime == sleepLog.bedTime.toString()
        responseBody.quality == sleepLog.quality.toString()
        responseBody.date == sleepLog.getSleepDate().toString()
        responseBody.totalSleepTime == sleepLog.getTotalSleepTimeInBed().toString()
    }

    def "Defaults to today if no date is provided"() {
        given: "A user has a sleep log for today"
        def userId = UUID.randomUUID()
        def sleepLog = TestEntitiesBuilder.buildSleepLog()
                .bedTime(LocalDate.now().atStartOfDay().minusHours(3))
                .wakeUpTime(LocalDate.now().atStartOfDay().plusHours(4))
                .build()
        saveSleepLogRepository.save(sleepLog, userId)

        when: "A user requests a sleep log without specifying a date"
        def response = createHttpRequest()
                .with().header(Constants.USER_ID_HEADER, userId.toString())
                .when().get(SleepLogsApiV1.GET_SLEEP_LOG_FROM_SPECIFIC_DATE)

        then: "The response status is OK"
        response.statusCode() == HttpStatus.OK.value()

        and: "sleep log data is returned in the response body"
        def responseBody = response.body().as(GetSleepLogFromSpecificDateHttpResponse)
        responseBody.wakeUpTime == sleepLog.wakeUpTime.toString()
        responseBody.bedTime == sleepLog.bedTime.toString()
        responseBody.quality == sleepLog.quality.toString()
        responseBody.date == LocalDate.now().toString()
        responseBody.totalSleepTime == sleepLog.getTotalSleepTimeInBed().toString()
    }

    def "Date is not in correct format"() {
        when: "A user requests a sleep log with an invalid date format"
        def response = createHttpRequest()
                .with().header(Constants.USER_ID_HEADER, UUID.randomUUID().toString())
                .and().queryParam("date", "invalid-date-format")
                .when().get(SleepLogsApiV1.GET_SLEEP_LOG_FROM_SPECIFIC_DATE)

        then: "The response status is BAD_REQUEST"
        response.statusCode() == HttpStatus.BAD_REQUEST.value()

        and: "response contains a meaningful error message"
        def responseBody = response.body().asString()
        responseBody.contains("Invalid date")
        responseBody.contains("yyyy-MM-dd")
    }
}
