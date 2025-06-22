package com.noom.interview.fullstack.sleep.api.v1;

import com.noom.interview.fullstack.sleep.api.commons.Constants;
import com.noom.interview.fullstack.sleep.api.v1.requests.CreateSleepLogHttpRequest;
import com.noom.interview.fullstack.sleep.api.v1.responses.GetSleepLogFromSpecificDateHttpResponse;
import com.noom.interview.fullstack.sleep.infrastructure.validation.ValidDate;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import java.util.UUID;

@Validated
public interface SleepLogsApiV1 {
    String BASE_PATH = "sleep-management/api/v1";
    String CREATE_SLEEP_LOG = BASE_PATH + "/sleep-logs";
    String GET_SLEEP_LOG_FROM_SPECIFIC_DATE = BASE_PATH + "/sleep-logs";

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(CREATE_SLEEP_LOG)
    void createSleepLog(
            @RequestHeader(value = Constants.USER_ID_HEADER) UUID userId,
            @RequestBody @Valid CreateSleepLogHttpRequest request
    );

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(GET_SLEEP_LOG_FROM_SPECIFIC_DATE)
    GetSleepLogFromSpecificDateHttpResponse getSleepLogFromSpecificDate(
            @RequestHeader(value = Constants.USER_ID_HEADER) UUID userId,
            @RequestParam(value = "date", required = false) @ValidDate(message = "Invalid date. Expected format: yyyy-MM-dd") String date
    );
}
