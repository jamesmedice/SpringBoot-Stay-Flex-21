package com.medici.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * MultipleProcessEvent
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultipleProcessEventRequest {

    private UUID processUuid;

    private String eventName;

    @Valid
    private List<@Valid AdditionalProperties> params;

}

