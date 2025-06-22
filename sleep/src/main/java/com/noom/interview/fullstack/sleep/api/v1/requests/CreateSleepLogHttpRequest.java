package com.noom.interview.fullstack.sleep.api.v1.requests;

import com.noom.interview.fullstack.sleep.domain.SleepQuality;
import com.noom.interview.fullstack.sleep.infrastructure.validation.ValidDateTime;
import com.noom.interview.fullstack.sleep.infrastructure.validation.ValidEnum;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateSleepLogHttpRequest {
    @ValidDateTime
    private String bedTimeAndDate;

    @ValidDateTime
    private String wakeUpTimeAndDate;

    @NotNull
    @ValidEnum(enumClass = SleepQuality.class)
    private String quality;
}
