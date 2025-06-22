package com.noom.interview.fullstack.sleep.testutils

import com.noom.interview.fullstack.sleep.api.v1.requests.CreateSleepLogHttpRequest
import com.noom.interview.fullstack.sleep.api.v1.responses.GetSleepLogFromSpecificDateHttpResponse
import com.noom.interview.fullstack.sleep.domain.SleepQuality

import java.time.LocalDateTime

class TestRequestsBuilder {
    static CreateSleepLogHttpRequest.CreateSleepLogHttpRequestBuilder buildCreateSleepLogHttpRequest() {
        return CreateSleepLogHttpRequest.builder()
                .bedTimeAndDate(LocalDateTime.now().minusHours(8).toString())
                .wakeUpTimeAndDate(LocalDateTime.now().toString())
                .quality(SleepQuality.GOOD.toString())
    }
}
