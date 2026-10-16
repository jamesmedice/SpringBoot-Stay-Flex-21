package com.medici.app.workflow.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublishExecutionRequest implements MessageRequest {

    private String title;
    private String subject;
    private String body;
    private String email;

    @Override
    public String getType() {
        return MessageRequestEnum.PublishExecution.toString();
    }
}
