package com.medici.app.model;

import com.medici.app.workflow.request.MessageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * SingleProcessEvent
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleProcessEventRequest<T extends MessageRequest> {

    private UUID processUuid;

    private String eventName;

    private String actionName;

    private T messageRequest;

}

