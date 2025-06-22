package com.noom.interview.fullstack.sleep.api.v1;

import com.noom.interview.fullstack.sleep.api.commons.Constants;
import com.noom.interview.fullstack.sleep.api.v1.requests.CreateSleepLogHttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import java.util.UUID;

public interface SleepLogsApiV1 {
    String BASE_PATH = "sleep-management/api/v1";
    String CREATE_SLEEP_LOG = BASE_PATH + "/sleep-logs";

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(CREATE_SLEEP_LOG)
    void createSleepLog(
            @RequestHeader(value = Constants.USER_ID_HEADER) UUID userId,
            @RequestBody @Valid CreateSleepLogHttpRequest request
    );
}
