package com.noom.interview.fullstack.sleep.api.v1.docs;

import com.noom.interview.fullstack.sleep.api.commons.Constants;
import com.noom.interview.fullstack.sleep.api.v1.SleepLogsApiV1;
import com.noom.interview.fullstack.sleep.api.v1.requests.CreateSleepLogHttpRequest;
import com.noom.interview.fullstack.sleep.api.v1.responses.GetSleepLogFromSpecificDateHttpResponse;
import com.noom.interview.fullstack.sleep.api.v1.responses.GetSleepSummaryHttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.UUID;

public interface SleepLogsApiV1Docs extends SleepLogsApiV1 {

    @Override
    @Operation(
        summary = "Create a new sleep log",
        description = "Records a new sleep log for the specified user.",
        parameters = {
            @Parameter(
                name = Constants.USER_ID_HEADER,
                description = "User identifier",
                required = true,
                in = ParameterIn.HEADER,
                example = "123e4567-e89b-12d3-a456-426614174000"
            )
        },
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(implementation = CreateSleepLogHttpRequest.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Sleep log created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
        }
    )
    void createSleepLog(
            @RequestHeader(value = Constants.USER_ID_HEADER) UUID userId,
            @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Sleep log creation request",
                required = true,
                content = @Content(schema = @Schema(implementation = CreateSleepLogHttpRequest.class))
            )
            CreateSleepLogHttpRequest request
    );

    @Override
    @Operation(
        summary = "Get sleep log for a specific date",
        description = "Retrieves the sleep log for the specified user and date.",
        parameters = {
            @Parameter(
                name = Constants.USER_ID_HEADER,
                description = "User identifier",
                required = true,
                in = ParameterIn.HEADER,
                example = "123e4567-e89b-12d3-a456-426614174000"
            ),
            @Parameter(
                name = "date",
                description = "Date for which to retrieve the sleep log (format: yyyy-MM-dd)",
                required = false,
                in = ParameterIn.QUERY,
                example = "2024-06-10",
                schema = @Schema(type = "string", format = "date", defaultValue = "2024-06-10")
            )
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Sleep log retrieved successfully",
                content = @Content(schema = @Schema(implementation = GetSleepLogFromSpecificDateHttpResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sleep log not found", content = @Content)
        }
    )
    GetSleepLogFromSpecificDateHttpResponse getSleepLogFromSpecificDate(
            @RequestHeader(value = Constants.USER_ID_HEADER) UUID userId,
            @RequestParam(value = "date", required = false)
            String date
    );

    @Override
    @Operation(
        summary = "Get sleep summary for a date range",
        description = "Returns a summary of the user's sleep logs between two dates (inclusive).",
        parameters = {
            @Parameter(
                name = Constants.USER_ID_HEADER,
                description = "User identifier",
                required = true,
                in = ParameterIn.HEADER,
                example = "123e4567-e89b-12d3-a456-426614174000"
            ),
            @Parameter(
                name = "startDate",
                description = "Start date of the summary range (format: yyyy-MM-dd)",
                required = false,
                in = ParameterIn.QUERY,
                example = "2024-06-01",
                schema = @Schema(type = "string", format = "date", defaultValue = "2024-06-01")
            ),
            @Parameter(
                name = "endDate",
                description = "End date of the summary range (format: yyyy-MM-dd)",
                required = false,
                in = ParameterIn.QUERY,
                example = "2024-06-10",
                schema = @Schema(type = "string", format = "date", defaultValue = "2024-06-10")
            )
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Sleep summary retrieved successfully",
                content = @Content(schema = @Schema(implementation = GetSleepSummaryHttpResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid date range", content = @Content)
        }
    )
    GetSleepSummaryHttpResponse getSleepSummary(
            @RequestHeader(value = Constants.USER_ID_HEADER) UUID userId,
            @RequestParam(value = "startDate", required = false)
            String startDate,
            @RequestParam(value = "endDate", required = false)
            String endDate
    );
}
