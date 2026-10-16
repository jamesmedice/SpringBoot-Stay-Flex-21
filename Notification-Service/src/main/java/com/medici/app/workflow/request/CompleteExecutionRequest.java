package com.medici.app.workflow.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompleteExecutionRequest implements MessageRequest {

    private String outcome;
    private String result;
    private String datasource;

    @Override
    public String getType() {
        return MessageRequestEnum.CompleteExecution.toString();
    }
}
