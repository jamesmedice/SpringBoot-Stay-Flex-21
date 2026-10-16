package com.medici.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventHistoryResponse {

    private UUID processId;

    private String eventName;

    private Date eventTime;

    private String destinationStep;

    private String eventUser;

    private String eventNote;

}