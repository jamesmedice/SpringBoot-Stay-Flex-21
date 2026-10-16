package com.medici.app.workflow.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeExecutionRequest implements MessageRequest {

    private String topic;
    private String subscription;

    @Override
    public String getType() {
        return MessageRequestEnum.SubscribeExecution.toString();
    }
}
