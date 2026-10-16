package com.medici.app.model;

import com.medici.app.workflow.request.MessageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalProperties<T extends MessageRequest> {

    private String actionName;

    private T messageRequest;

}

