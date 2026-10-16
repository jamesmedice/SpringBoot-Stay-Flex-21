package com.medici.app.workflow.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageExecutionRequest implements MessageRequest {

    private String bucket;
    private String folder;
    private String serviceAccount;

    @Override
    public String getType() {
        return MessageRequestEnum.StorageExecution.toString();
    }
}
