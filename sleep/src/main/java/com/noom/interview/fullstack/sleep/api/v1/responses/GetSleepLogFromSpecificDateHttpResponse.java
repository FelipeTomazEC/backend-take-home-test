package com.noom.interview.fullstack.sleep.api.v1.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetSleepLogFromSpecificDateHttpResponse {
    private String date;
    private String totalSleepTime;
    private String bedTime;
    private String wakeUpTime;
    private String quality;
}
