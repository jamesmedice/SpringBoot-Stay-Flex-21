package com.medici.app.model;

import com.medici.app.workflow.request.MessageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionResponse {

    private String actionName;
    private String actionClass;

    private MessageRequest messageRequest;

}
