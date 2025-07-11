package com.noom.interview.fullstack.sleep.infrastructure.controllers

import com.noom.interview.fullstack.sleep.api.commons.Constants
import com.noom.interview.fullstack.sleep.api.v1.SleepLogsApiV1
import com.noom.interview.fullstack.sleep.application.ports.repositories.SaveSleepLogRepository
import com.noom.interview.fullstack.sleep.domain.SleepQuality
import com.noom.interview.fullstack.sleep.testutils.AbstractControllerTest
import com.noom.interview.fullstack.sleep.testutils.TestEntitiesBuilder
import com.noom.interview.fullstack.sleep.testutils.TestRequestsBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import spock.lang.Unroll

import java.time.LocalDateTime

import static java.time.LocalDateTime.now

class CreateSleepLogHttpControllerTest extends AbstractControllerTest {

    @Autowired
    SaveSleepLogRepository saveSleepLogRepository

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate

    @Unroll
    def "Field #fieldName cannot be null"() {
        given: "a request to create a required field null"
        def userId = UUID.randomUUID()
        def requestBody = TestRequestsBuilder.buildCreateSleepLogHttpRequest()
                .bedTimeAndDate(bedTimeAndDate)
                .wakeUpTimeAndDate(wakeUpTimeAndDate)
                .quality(quality)
                .build()

        when: "the request is sent"
        def response = createHttpRequest()
                .with().header(Constants.USER_ID_HEADER, userId.toString())
                .body(requestBody)
                .post(SleepLogsApiV1.CREATE_SLEEP_LOG)

        then: "the response status is 400 Bad Request"
        response.statusCode() == HttpStatus.BAD_REQUEST.value()

        and: "the response body contains an error message"
        def responseBody = response.body().asString()
        responseBody.contains(fieldName)
        responseBody.contains("invalid")


        where:
        fieldName                            | bedTimeAndDate                          | wakeUpTimeAndDate      | quality
        "bedTimeAndDate"            | null                                                   | now().toString()                | SleepQuality.GOOD.name()
        "wakeUpTimeAndDate"    | now().minusHours(8).toString()   | null                                      | SleepQuality.BAD.name()
        "quality"                               | now().minusHours(8).toString()   | now().toString()                | null
    }

    def "Field quality must be a valid sleep quality"() {
        given: "a request with an invalid sleep quality"
        def userId = UUID.randomUUID()
        def requestBody = TestRequestsBuilder.buildCreateSleepLogHttpRequest()
                .quality("INVALID_QUALITY")
                .build()

        when: "the request is sent"
        def response = createHttpRequest()
                .with().header(Constants.USER_ID_HEADER, userId.toString())
                .body(requestBody)
                .post(SleepLogsApiV1.CREATE_SLEEP_LOG)

        then: "the response status is 400 Bad Request"
        response.statusCode() == HttpStatus.BAD_REQUEST.value()

        and: "the response body contains an error message"
        def responseBody = response.body().asString()
        responseBody.contains("quality")
        responseBody.contains("invalid")
        responseBody.contains(SleepQuality.values().collect { it.name() }.join(", "))
    }

    @Unroll
    def " Field #field must be a valid date-time string"() {
        given: "a request with invalid date-time string"
        def userId = UUID.randomUUID()
        def requestBody = TestRequestsBuilder.buildCreateSleepLogHttpRequest()
                .bedTimeAndDate(bedTimeAndDate)
                .wakeUpTimeAndDate(wakeUpTimeAndDate)
                .build()

        when: "the request is sent"
        def response = createHttpRequest()
                .with().header(Constants.USER_ID_HEADER, userId.toString())
                .body(requestBody)
                .post(SleepLogsApiV1.CREATE_SLEEP_LOG)

        then: "the response status is 400 Bad Request"
        response.statusCode() == HttpStatus.BAD_REQUEST.value()

        and: "the response body contains an error message"
        def responseBody = response.body().asString()
        responseBody.contains(field)
        responseBody.contains("invalid")

        where:
        field                                      | bedTimeAndDate                          | wakeUpTimeAndDate
        "bedTimeAndDate"           | "invalid-date-time"                        | now().toString()
        "bedTimeAndDate"           | "2023-13-01T00:00:00"                | now().toString()
        "wakeUpTimeAndDate"   | now().minusHours(8).toString()   | "invalid-date-time"
        "wakeUpTimeAndDate"   | now().minusHours(8).toString()   | "2023-13-01T00:00:00"
    }

    def "A sleep log must have a person associated with it"() {
        given: "a request to create a sleep log"
        def requestBody = TestRequestsBuilder.buildCreateSleepLogHttpRequest().build()

        and: "the user ID header is not set"
        def request = createHttpRequest()
                .with().header(Constants.USER_ID_HEADER, "")
                .body(requestBody)

        when: "the request is sent"
        def response = request.post(SleepLogsApiV1.CREATE_SLEEP_LOG)

        then: "the response status is 401 Unauthorized"
        response.statusCode() == HttpStatus.UNAUTHORIZED.value()
    }

    def "A sleep log can be created successfully"() {
        given: "a request to create a sleep log"
        def userId = UUID.randomUUID()
        def requestBody = TestRequestsBuilder.buildCreateSleepLogHttpRequest().build()

        when: "the request is sent"
        def response = createHttpRequest()
                .with().header(Constants.USER_ID_HEADER, userId.toString())
                .body(requestBody)
                .post(SleepLogsApiV1.CREATE_SLEEP_LOG)

        then: "the response status is 201 Created"
        response.statusCode() == HttpStatus.CREATED.value()

        and: "a sleep log is saved in the database"
        def retrievedLog = retrieveSleepLogByUserId(userId)
        retrievedLog.bedTime.withNano(0) == LocalDateTime.parse(requestBody.bedTimeAndDate).toLocalTime().withNano(0)
        retrievedLog.wakeUpTime.withNano(0) == LocalDateTime.parse(requestBody.wakeUpTimeAndDate).toLocalTime().withNano(0)
        retrievedLog.quality == SleepQuality.valueOf(requestBody.quality)
    }

    def "Cannot have more than one sleep log per day"() {
        given: "a user has an existing sleep log for a given day"
        def userId = UUID.randomUUID()
        def existingLog = TestEntitiesBuilder.buildSleepLog().build()
        saveSleepLogRepository.save(existingLog, userId)

        and: "we try to create a new sleep log for the same day"
        def newLogRequest = TestRequestsBuilder.buildCreateSleepLogHttpRequest()
                .bedTimeAndDate(existingLog.getSleepDate().minusDays(1).atTime(22, 0).toString())
                .wakeUpTimeAndDate(existingLog.getSleepDate().atTime(6, 0).toString())
                .build()

        when: "the request is sent"
        def response = createHttpRequest()
                .with().header(Constants.USER_ID_HEADER, userId.toString())
                .body(newLogRequest)
                .post(SleepLogsApiV1.CREATE_SLEEP_LOG)

        then: "the response status is 409 Conflict"
        response.statusCode() == HttpStatus.CONFLICT.value()

        and: "the response body contains an error message"
        def responseBody = response.body().asString()
        responseBody.contains("already exists")
    }

    def "Sleep log cannot be created for a date in the future"() {
        given: "a request to create a sleep log with a future date"
        def userId = UUID.randomUUID()
        def futureDate = now().plusDays(1)
        def requestBody = TestRequestsBuilder.buildCreateSleepLogHttpRequest()
                .bedTimeAndDate(futureDate.toString())
                .wakeUpTimeAndDate(futureDate.plusHours(8).toString())
                .build()

        when: "the request is sent"
        def response = createHttpRequest()
                .with().header(Constants.USER_ID_HEADER, userId.toString())
                .body(requestBody)
                .post(SleepLogsApiV1.CREATE_SLEEP_LOG)

        then: "the response status is 400 Bad Request"
        response.statusCode() == HttpStatus.BAD_REQUEST.value()

        and: "the response body contains an error message"
        def responseBody = response.body().asString()
        responseBody.contains("cannot be in the future")
    }
}
