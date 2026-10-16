package com.medici.app.workflow.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeExecutionResponse implements MessageResponse {

    private Date received;
    private String topic;
    private String subscription;

}
