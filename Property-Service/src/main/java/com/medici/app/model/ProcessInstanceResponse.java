package com.medici.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * ProcessInstanceResponse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessInstanceResponse {

    private UUID processId;

    private String processName;

    private String processVersion;

    private String processStatus;

    private String currentStep;

    private String parentProcessId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime creationTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime currentStepEntryTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime closingTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime updateTime;

}

