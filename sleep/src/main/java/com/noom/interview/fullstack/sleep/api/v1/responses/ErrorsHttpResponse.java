package com.noom.interview.fullstack.sleep.api.v1.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class ErrorsHttpResponse {
    private Set<String> errors;
}
